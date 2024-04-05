package com.example.subscriptionservice.model;

import lombok.Builder;

@Builder
public record NotificationModel(String username, String blogTitle, String email) {

}
