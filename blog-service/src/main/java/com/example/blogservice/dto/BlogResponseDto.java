package com.example.blogservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Represents a DTO (Data Transfer Object) for a blog response.
 */
@Data
@Builder
public class BlogResponseDto {

    private Long id;

    private String title;

    private String description;

    private String ownerId;

}
