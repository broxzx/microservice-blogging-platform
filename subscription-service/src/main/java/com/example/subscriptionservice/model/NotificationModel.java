package com.example.subscriptionservice.model;

import lombok.Builder;

/**
 * The NotificationModel class represents a notification for a blog user.
 */
@Builder
public record NotificationModel(String username, String blogTitle, String email) {

}
