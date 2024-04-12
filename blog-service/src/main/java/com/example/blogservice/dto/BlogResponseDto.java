package com.example.blogservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogResponseDto {

    private Long id;

    private String title;

    private String description;

    private String ownerId;

}
