package com.example.notificationservice.model;

/**
 * NotificationModel is a record class used to store information about a notification,
 * including the subscriber's username, blog title, and email.
 */
public record NotificationModel(String username, String blogTitle, String email) {

}
