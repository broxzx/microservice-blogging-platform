package com.messageservice.messageservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents a data transfer object (DTO) for a message response.
 * It contains the information related to a message, such as its ID,
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
