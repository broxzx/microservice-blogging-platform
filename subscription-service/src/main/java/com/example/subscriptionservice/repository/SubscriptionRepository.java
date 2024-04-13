package com.example.subscriptionservice.repository;

import com.example.subscriptionservice.entity.SubscriptionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * The SubscriptionRepository interface is responsible for defining the contract
 * for accessing the subscription data stored in a MongoDB database.
 */
public interface SubscriptionRepository extends MongoRepository<SubscriptionEntity, Long> {

    /**
     * Retrieves a list of SubscriptionEntities based on the given user ID.
     *
     * @param userId the ID of the user
     * @return a list of SubscriptionEntities associated with the given user ID
     */
    List<SubscriptionEntity> findByUserId(Long userId);

    /**
     * Retrieves a list of SubscriptionEntities associated with the given blog ID.
     *
     * @param blogId the ID of the blog
     * @return a list of SubscriptionEntities associated with the given blog ID
     */
    List<SubscriptionEntity> findByBlogId(Long blogId);

}
