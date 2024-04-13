package com.example.blogservice.dto;

import lombok.Builder;
import lombok.Data;

/**
*  Represents a DTO (Data Transfer Object) for a blog request.
 */
@Data
@Builder
public class BlogRequestDto {

    private String title;

    private String description;
}
