package com.example.blogservice.utils;

import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.entity.BlogEntity;
import org.springframework.stereotype.Component;

/**
 * Represents a factory class that creates instances of BlogResponseDto.
 * This class provides a method to create a BlogResponseDto object based on a BlogEntity object.
 */
@Component
public class BlogResponseDtoFactory {

    /**
     * Creates a BlogResponseDto object based on a BlogEntity object.
     *
     * @param blog the BlogEntity object to create the BlogResponseDto from
     * @return the created BlogResponseDto object
     */
    public BlogResponseDto makeBlogResponseDto(BlogEntity blog) {
        return BlogResponseDto.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .description(blog.getDescription())
                .ownerId(blog.getOwnerId())
                .build();
    }
}
