package com.example.subscriptionservice.repository;

import com.example.entityservice.entity.SubscriptionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubscriptionRepository extends MongoRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findByUserId(String userId);

    List<SubscriptionEntity> findByBlogId(String blogId);

    void deleteByUserIdAndBlogId(String userId, String blogId);
}
