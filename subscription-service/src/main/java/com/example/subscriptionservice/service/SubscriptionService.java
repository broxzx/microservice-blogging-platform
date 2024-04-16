package com.example.subscriptionservice.service;

import com.example.subscriptionservice.dto.SubscriptionRequest;
import com.example.subscriptionservice.entity.SubscriptionEntity;
import com.example.subscriptionservice.exception.NotFoundException;
import com.example.subscriptionservice.exception.TokenIsInvalidException;
import com.example.subscriptionservice.model.BlogModelResponse;
import com.example.subscriptionservice.model.NotificationModel;
import com.example.subscriptionservice.model.UserModelResponse;
import com.example.subscriptionservice.producer.RabbitMQNotificationProducer;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.List;
import java.util.Objects;

/**
 * The SubscriptionService class is responsible for managing subscriptions and sending notifications.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final BlogService blogService;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final RabbitMQNotificationProducer rabbitMQNotificationProducer;

    private final WebClient webClient;

    private final Tracer tracer;

    private static final String SEQUENCE_NAME = "subscription_sequence";


    /**
     * Retrieves all subscriptions from the database.
     *
     * @return A list of SubscriptionEntity objects representing all the subscriptions.
     */
    public List<SubscriptionEntity> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    /**
     * Retrieves a subscription entity by its unique identifier.
     *
     * @param id The unique identifier of the subscription to retrieve.
     * @return The subscription entity with the given id.
     * @throws NotFoundException if no subscription with the specified id is found.
     */
    public SubscriptionEntity getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("subscription with id %d was not found".formatted(id))
                );
    }

    /**
     * Save the given subscription entity.
     *
     * @param subscriptionEntity the subscription entity to be saved
     */
    public void saveSubscription(SubscriptionEntity subscriptionEntity) {
        subscriptionRepository.save(subscriptionEntity);
    }

    /**
     * Saves a subscription entity and sends a notification to the subscriber.
     *
     * @param subscriptionRequest the subscription request object containing the blog ID and user ID
     * @return the saved subscription entity
     */
    public SubscriptionEntity saveSubscriptionAndSendNotification(SubscriptionRequest subscriptionRequest) {
        BlogModelResponse blogModelResponse = blogService.getBlogEntityById(subscriptionRequest.getBlogId());

        UserModelResponse userModelResponse;
        Span userEntityLookUp = tracer.nextSpan().name("User Entity LookUp");
        try (Tracer.SpanInScope ignored = tracer.withSpan(userEntityLookUp.start())) {
            userModelResponse = webClient
                    .get()
                    .uri("http://localhost:8080/user/by-jwt-token", uriBuilder -> uriBuilder
                            .queryParam("token", getSubstringUserToken()).build())
                    .header(HttpHeaders.AUTHORIZATION, getUserToken())
                    .retrieve()
                    .bodyToMono(UserModelResponse.class)
                    .block();
        } finally {
            userEntityLookUp.end();
        }

        log.info(userModelResponse);

        SubscriptionEntity subscriptionEntity = SubscriptionEntity.builder()
                .id(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .blogId(subscriptionRequest.getBlogId())
                .userId(subscriptionRequest.getUserId())
                .username(Objects.requireNonNull(userModelResponse).username())
                .blogName(blogModelResponse.title())
                .build();

        subscriptionRepository.save(subscriptionEntity);

        NotificationModel notificationModel = NotificationModel.builder()
                .username(subscriptionEntity.getUsername())
                .blogTitle(subscriptionEntity.getBlogName())
                .email(userModelResponse.email())
                .build();

        rabbitMQNotificationProducer.sendNotificationToSubscriber(notificationModel);
        sendNotificationToBlogOwner(blogModelResponse.ownerId(), blogModelResponse.title());

        return subscriptionEntity;
    }

    /**
     * Sends a notification to the blog owner.
     *
     * @param userId    the ID of the user to send the notification to
     * @param blogTitle the title of the blog
     */
    private void sendNotificationToBlogOwner(String userId, String blogTitle) {
        Span userEntityLookUp = tracer.nextSpan().name("User Entity LookUp");

        UserModelResponse response;
        try (Tracer.SpanInScope ignored = tracer.withSpan(userEntityLookUp.start())) {
            response = webClient
                    .get()
                    .uri("http://localhost:8080/user/by-jwt-token", uriBuilder -> uriBuilder
                            .queryParam("token", getSubstringUserToken()).build())
                    .header(HttpHeaders.AUTHORIZATION, getUserToken())
                    .retrieve()
                    .bodyToMono(UserModelResponse.class)
                    .block();
        } finally {
            userEntityLookUp.end();
        }

        NotificationModel notificationModel = NotificationModel.builder()
                .username(Objects.requireNonNull(response).username())
                .email(response.email())
                .blogTitle(blogTitle)
                .build();

        rabbitMQNotificationProducer.sendNotificationToBlogOwner(notificationModel);
    }

    /**
     * Updates the given subscription entity with the information from the subscription request.
     *
     * @param subscriptionEntity  The subscription entity to be updated.
     * @param subscriptionRequest The subscription request containing the new information.
     * @return The updated subscription entity.
     * @throws WebClientException when there is an error with the web client communication.
     */
    public SubscriptionEntity updateSubscription(SubscriptionEntity subscriptionEntity, SubscriptionRequest subscriptionRequest) {
        BlogModelResponse foundBlogEntity = blogService.getBlogEntityById(subscriptionRequest.getBlogId());
        UserModelResponse userModelResponse;

        Span userEntityLookUp = tracer.nextSpan().name("User Entity LookUp");


        try (Tracer.SpanInScope ignored = tracer.withSpan(userEntityLookUp.start())) {
            userModelResponse = webClient
                    .get()
                    .uri("http://localhost:8080/user/%d".formatted(subscriptionRequest.getUserId()))
                    .retrieve()
                    .bodyToMono(UserModelResponse.class)
                    .block();
        } finally {
            userEntityLookUp.end();
        }
        log.info(userModelResponse);

        subscriptionEntity.setBlogId(foundBlogEntity.id());
        subscriptionEntity.setUserId(Long.valueOf(Objects.requireNonNull(userModelResponse).userId()));
        subscriptionEntity.setBlogName(foundBlogEntity.title());
        subscriptionEntity.setUsername(userModelResponse.username());

        return subscriptionEntity;
    }

    /**
     * Deletes a subscription by its ID.
     *
     * @param id the ID of the subscription to be deleted
     */
    public void deleteSubscriptionById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    /**
     * Retrieves a list of subscription entities based on the given user ID.
     *
     * @param userId the user ID used to search for subscriptions
     * @return a list of subscription entities that match the given user ID
     */
    public List<SubscriptionEntity> findSubscriptionByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    /**
     * Finds a list of subscription entities by the given blog ID.
     *
     * @param blogId the ID of the blog to search for subscriptions
     * @return a list of subscription entities matching the given blog ID
     */
    public List<SubscriptionEntity> findSubscriptionByBlogId(Long blogId) {
        return subscriptionRepository.findByBlogId(blogId);
    }


    public String getUserToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            return token;
        } else {
            throw new TokenIsInvalidException("Authorization header is invalid");
        }
    }

    public String getSubstringUserToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return token;
        } else {
            throw new TokenIsInvalidException("Authorization header is invalid");
        }
    }
}
