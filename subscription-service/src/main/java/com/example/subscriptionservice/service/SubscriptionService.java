package com.example.subscriptionservice.service;

import com.example.subscriptionservice.dto.SubscriptionRequest;
import com.example.subscriptionservice.model.UserModelResponse;
import com.example.subscriptionservice.entity.BlogEntity;
import com.example.subscriptionservice.entity.SubscriptionEntity;
import com.example.subscriptionservice.exception.NotFoundException;
import com.example.subscriptionservice.model.NotificationModel;
import com.example.subscriptionservice.producer.RabbitMQNotificationProducer;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final BlogService blogService;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final RabbitMQNotificationProducer rabbitMQNotificationProducer;

    private final WebClient webClient;

    private static final String SEQUENCE_NAME = "subscription_sequence";


    public List<SubscriptionEntity> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public SubscriptionEntity getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException("subscription with id %d was not found".formatted(id))
                );
    }

    public void saveSubscriptionAndSendNotification(SubscriptionEntity subscriptionEntity) {
        subscriptionRepository.save(subscriptionEntity);
    }

    public SubscriptionEntity saveSubscriptionAndSendNotification(SubscriptionRequest subscriptionRequest) {
        BlogEntity foundBlogEntity = blogService.getBlogEntityById(subscriptionRequest.getBlogId());
        UserModelResponse userModelResponse = webClient
                .get()
                .uri("http://localhost:8080/user/%d".formatted(subscriptionRequest.getUserId()))
                .retrieve()
                .bodyToMono(UserModelResponse.class)
                .block();

        log.info(userModelResponse);

        SubscriptionEntity builtUser = SubscriptionEntity.builder()
                .id(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .blogId(subscriptionRequest.getBlogId())
                .userId(subscriptionRequest.getUserId())
                .username(Objects.requireNonNull(userModelResponse).username())
                .blogName(foundBlogEntity.getTitle())
                .build();

        subscriptionRepository.save(builtUser);

        NotificationModel notificationModel = NotificationModel.builder()
                .username(builtUser.getUsername())
                .blogTitle(builtUser.getBlogName())
                .email(userModelResponse.email())
                .build();

        rabbitMQNotificationProducer.sendNotification(notificationModel);

        return builtUser;
    }

    public SubscriptionEntity updateSubscription(SubscriptionEntity subscriptionEntity, SubscriptionRequest subscriptionRequest) {
        BlogEntity foundBlogEntity = blogService.getBlogEntityById(subscriptionRequest.getBlogId());
        UserModelResponse userModelResponse = webClient
                .get()
                .uri("http://localhost:8080/user/%d".formatted(subscriptionRequest.getUserId()))
                .retrieve()
                .bodyToMono(UserModelResponse.class)
                .block();

        log.info(userModelResponse);

        subscriptionEntity.setBlogId(foundBlogEntity.getId());
        subscriptionEntity.setUserId(Objects.requireNonNull(userModelResponse).userId());
        subscriptionEntity.setBlogName(foundBlogEntity.getTitle());
        subscriptionEntity.setUsername(userModelResponse.username());

        return subscriptionEntity;
    }

    public void deleteSubscriptionById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public List<SubscriptionEntity> findSubscriptionByUserId(Long userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public List<SubscriptionEntity> findSubscriptionByBlogId(Long blogId) {
        return subscriptionRepository.findByBlogId(blogId);
    }
}
