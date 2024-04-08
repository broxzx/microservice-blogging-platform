package com.example.subscriptionservice.service;

import com.example.subscriptionservice.entity.BlogEntity;
import com.example.subscriptionservice.exception.NotFoundException;
import com.example.subscriptionservice.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public BlogEntity getBlogEntityById(Long blogId) {
        return blogRepository.findById(blogId)
                .orElseThrow(
                        () -> new NotFoundException("blog with id %d wasn't found".formatted(blogId))
                );
    }


}
