package com.example.subscriptionservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a subscription request containing the blog and user IDs.
 */
@Data
@Builder
public class SubscriptionRequest {

    private Long blogId;

    private Long userId;
}
