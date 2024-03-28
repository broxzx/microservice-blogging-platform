package com.example.blogservice.controller;

import com.example.blogservice.dto.BlogRequestDto;
import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.exception.BlogNotFoundException;
import com.example.blogservice.repository.BlogRepository;
import com.example.blogservice.service.SequenceGeneratorService;
import com.example.blogservice.utils.BlogResponseDtoFactory;
import com.example.entityservice.entity.BlogEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogRepository repository;

    private final BlogResponseDtoFactory blogResponseDtoFactory;

    private final SequenceGeneratorService sequenceGeneratorService;

    private static final String GET_ALL_BLOGS = "/";
    private static final String GET_BLOG_BY_ID = "/{id}";
    private static final String CREATE_BLOG = "/";


    @GetMapping(GET_ALL_BLOGS)
    public ResponseEntity<List<BlogResponseDto>> getAllBlogs() {
        List<BlogEntity> blogs = repository.findAll();

        List<BlogResponseDto> response = blogs
                .stream()
                .map(blogResponseDtoFactory::makeBlogResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping(GET_BLOG_BY_ID)
    private ResponseEntity<BlogResponseDto> getBlogById(@PathVariable Long id) {
        Optional<BlogEntity> foundBlogEntity = repository.findById(id);

        BlogResponseDto response = blogResponseDtoFactory.makeBlogResponseDto(
                foundBlogEntity.orElseThrow(
                        () -> new BlogNotFoundException("blog with id '%d' was not found".formatted(id))
                )
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(CREATE_BLOG)
    public ResponseEntity<String> createBlog(@RequestBody BlogRequestDto blogRequest) {
        BlogEntity createdBlogEntity = BlogEntity.builder()
                .id(sequenceGeneratorService.generateSequence("blog_sequence"))
                .title(blogRequest.getTitle())
                .description(blogRequest.getDescription())
                .build();

        repository.save(createdBlogEntity);

        return ResponseEntity.ok("blog with id '%d' was created".formatted(createdBlogEntity.getId()));
    }
}
