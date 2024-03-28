package com.example.blogservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlogRequestDto {

    private String title;

    private String description;
}
