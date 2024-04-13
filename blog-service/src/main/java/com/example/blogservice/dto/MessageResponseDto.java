package com.example.blogservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a DTO (Data Transfer Object) for a message response.
 */
@Data
@Builder
public class MessageResponseDto {

    private Long id;

    private String content;

    private String authorId;

    private String blogId;

    private LocalDateTime createdAt;
}
