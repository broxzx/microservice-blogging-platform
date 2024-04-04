package com.example.subscriptionservice.repository;

import com.example.entityservice.entity.SubscriptionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubscriptionRepository extends MongoRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findByUserId(Long userId);

    List<SubscriptionEntity> findByBlogId(Long blogId);

    void deleteByUserIdAndBlogId(Long userId, Long blogId);
}
