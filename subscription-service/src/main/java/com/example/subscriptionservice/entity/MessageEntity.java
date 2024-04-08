package com.example.subscriptionservice.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MessageEntity {

    @Transient
    private static final String SEQUENCE_NAME = "message_sequence";

    @Id
    private Long id;

    private String content;

    private String authorId;

    private String blogId;

    private LocalDateTime createdAt;
}