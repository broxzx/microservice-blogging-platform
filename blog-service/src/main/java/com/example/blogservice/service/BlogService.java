package com.example.blogservice.service;

import com.example.blogservice.dto.BlogRequestDto;
import com.example.blogservice.exception.BlogNotFoundException;
import com.example.blogservice.repository.BlogRepository;
import com.example.entityservice.entity.BlogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public List<BlogEntity> findAll() {
        return blogRepository.findAll();
    }

    public BlogEntity findById(Long id) {
        return blogRepository.findById(id).orElseThrow(
                () -> new BlogNotFoundException("blog with id '%d' was not found".formatted(id))
        );
    }

    public void save(BlogEntity blogEntity) {
        blogRepository.save(blogEntity);
    }

    public void delete(BlogEntity blogEntity) {
        blogRepository.delete(blogEntity);
    }

    public void updateBlogEntity(BlogRequestDto blogRequest, BlogEntity blogEntity) {
        blogEntity.setTitle(blogRequest.getTitle());
        blogEntity.setDescription(blogRequest.getDescription());
    }

}
