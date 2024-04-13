package com.example.subscriptionservice.utils;

import com.example.subscriptionservice.dto.SubscriptionResponse;
import com.example.subscriptionservice.entity.SubscriptionEntity;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for mapping SubscriptionEntity objects to SubscriptionResponse objects.
 */
@Component
public class SubscriptionResponseMapper {

    /**
     * Creates a SubscriptionResponse object based on the provided SubscriptionEntity.
     *
     * @param subscriptionEntity The SubscriptionEntity object to create the response from
     * @return The created SubscriptionResponse object
     */
    public SubscriptionResponse makeSubscriptionResponse(SubscriptionEntity subscriptionEntity) {
        return SubscriptionResponse.builder()
                .blogName(subscriptionEntity.getBlogName())
                .username(subscriptionEntity.getUsername())
                .build();
    }
}
