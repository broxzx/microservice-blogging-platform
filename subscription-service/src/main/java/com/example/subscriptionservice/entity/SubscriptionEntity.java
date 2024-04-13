package com.example.subscriptionservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a subscription entity that stores information about a user's subscription to a blog.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "subscription")
public class SubscriptionEntity {

    @Transient
    private static final String SEQUENCE_NAME = "subscription_sequence";

    @Id
    private Long id;

    private Long userId;

    private String username;

    private Long blogId;

    private String blogName;
}
