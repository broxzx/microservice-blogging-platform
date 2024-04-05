package com.example.entityservice.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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