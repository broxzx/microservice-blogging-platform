package com.example.subscriptionservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionRequest {

    private String userId;

    private String blogId;
}
