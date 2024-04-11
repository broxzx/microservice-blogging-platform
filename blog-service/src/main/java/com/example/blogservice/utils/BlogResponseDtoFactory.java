package com.example.blogservice.utils;

import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.entity.BlogEntity;
import org.springframework.stereotype.Component;

@Component
public class BlogResponseDtoFactory {

    public BlogResponseDto makeBlogResponseDto(BlogEntity blog) {
        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .description(blog.getDescription())
                .ownerId(blog.getOwnerId())
                .build();
    }
}
