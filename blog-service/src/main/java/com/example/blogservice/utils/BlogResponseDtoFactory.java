package com.example.blogservice.utils;

import com.example.blogservice.dto.BlogResponseDto;
import com.example.entityservice.entity.BlogEntity;
import org.springframework.stereotype.Component;

@Component
public class BlogResponseDtoFactory {

    public BlogResponseDto makeBlogResponseDto(BlogEntity blog) {
        return BlogResponseDto.builder()
                .title(blog.getTitle())
                .description(blog.getDescription())
                .messages(blog.getMessages())
                .build();
    }
}
