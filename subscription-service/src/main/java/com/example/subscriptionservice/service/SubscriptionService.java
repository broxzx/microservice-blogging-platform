package com.example.subscriptionservice.service;

import com.example.subscriptionservice.dto.SubscriptionRequest;
import com.example.subscriptionservice.entity.BlogEntity;
import com.example.subscriptionservice.entity.SubscriptionEntity;
import com.example.subscriptionservice.entity.UserEntity;
import com.example.subscriptionservice.exception.NotFoundException;
import com.example.subscriptionservice.model.NotificationModel;
import com.example.subscriptionservice.producer.RabbitMQNotificationProducer;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final BlogService blogService;

    private final UserService userService;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final RabbitMQNotificationProducer rabbitMQNotificationProducer;

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
        UserEntity foundUserEntity = userService.getUserEntityById(subscriptionRequest.getUserId());

        SubscriptionEntity builtUser = SubscriptionEntity.builder()
                .id(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .blogId(subscriptionRequest.getBlogId())
                .userId(subscriptionRequest.getUserId())
                .username(foundUserEntity.getUsername())
                .blogName(foundBlogEntity.getTitle())
                .build();

        subscriptionRepository.save(builtUser);

        NotificationModel notificationModel = NotificationModel.builder()
                .username(builtUser.getUsername())
                .blogTitle(builtUser.getBlogName())
                .email(foundUserEntity.getEmail())
                .build();

        rabbitMQNotificationProducer.sendNotification(notificationModel);

        return builtUser;
    }

    public SubscriptionEntity updateSubscription(SubscriptionEntity subscriptionEntity, SubscriptionRequest subscriptionRequest) {
        BlogEntity foundBlogEntity = blogService.getBlogEntityById(subscriptionRequest.getBlogId());
        UserEntity foundUserEntity = userService.getUserEntityById(subscriptionRequest.getUserId());

        subscriptionEntity.setBlogId(foundBlogEntity.getId());
        subscriptionEntity.setUserId(foundUserEntity.getId());
        subscriptionEntity.setBlogName(foundBlogEntity.getTitle());
        subscriptionEntity.setUsername(foundUserEntity.getUsername());

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
