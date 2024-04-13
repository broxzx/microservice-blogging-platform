package com.example.subscriptionservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Represents the response for a subscription.
 */
@Data
@Builder
public class SubscriptionResponse {

    private String username;

    private String blogName;
}
