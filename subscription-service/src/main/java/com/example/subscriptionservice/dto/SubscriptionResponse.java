package com.example.subscriptionservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriptionResponse {

    private String username;

    private String blogName;
}
