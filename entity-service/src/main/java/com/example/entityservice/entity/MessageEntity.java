package com.example.entityservice.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "messages")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageEntity {

    @Id
    private Long id;

    private String content;

    private String authorId;

    private LocalDateTime createdAt;
}
