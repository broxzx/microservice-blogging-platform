package com.messageservice.messageservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * This class represents a message entity, which is used to store information about a message.
 */
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