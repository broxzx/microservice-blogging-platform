package com.example.blogservice.model;

import java.time.LocalDateTime;

public record MessageModelResponse(Long id, String content, String authorId, String blogId, LocalDateTime createdAt) {
}
