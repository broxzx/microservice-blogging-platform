package com.example.subscriptionservice.utils;

import com.example.subscriptionservice.dto.SubscriptionResponse;
import com.example.subscriptionservice.entity.SubscriptionEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionResponseMapper {

    public SubscriptionResponse makeSubscriptionResponse(SubscriptionEntity subscriptionEntity) {
        return SubscriptionResponse.builder()
                .blogName(subscriptionEntity.getBlogName())
                .username(subscriptionEntity.getUsername())
                .build();
    }
}
