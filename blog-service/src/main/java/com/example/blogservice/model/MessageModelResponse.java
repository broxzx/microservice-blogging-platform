package com.example.blogservice.model;

import java.time.LocalDateTime;

/**
 * Represents a response model for a message.
 */
public record MessageModelResponse(Long id, String content, String authorId, String blogId, LocalDateTime createdAt) {
}
