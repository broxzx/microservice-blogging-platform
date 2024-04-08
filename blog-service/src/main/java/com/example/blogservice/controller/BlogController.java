package com.example.blogservice.controller;

import com.example.blogservice.dto.BlogRequestDto;
import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.entity.BlogEntity;
import com.example.blogservice.service.BlogService;
import com.example.blogservice.service.SequenceGeneratorService;
import com.example.blogservice.service.UserService;
import com.example.blogservice.utils.BlogResponseDtoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogResponseDtoFactory blogResponseDtoFactory;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final BlogService blogService;

    private final UserService userService;

    private static final String SEQUENCE_NAME = "blog_sequence";
    private static final String GET_ALL_BLOGS = "/";
    private static final String GET_BLOG_BY_ID = "/{id}";
    private static final String CREATE_BLOG = "/";
    private static final String UPDATE_BLOG_BY_ID = "/{id}";
    private static final String DELETE_BLOG_BY_ID = "/{id}";


    @GetMapping(GET_ALL_BLOGS)
    public ResponseEntity<List<BlogResponseDto>> getAllBlogs() {
        List<BlogEntity> blogs = blogService.findAll();

        List<BlogResponseDto> response = blogs
                .stream()
                .map(blogResponseDtoFactory::makeBlogResponseDto)
                .toList();

        return ResponseEntity
                .ok(response);
    }

    @GetMapping(GET_BLOG_BY_ID)
    public ResponseEntity<BlogResponseDto> getBlogById(@PathVariable Long id) {
        BlogEntity foundBlogEntity = blogService.findById(id);

        BlogResponseDto response = blogResponseDtoFactory.makeBlogResponseDto(foundBlogEntity);

        return ResponseEntity
                .ok(response);
    }

    @PostMapping(CREATE_BLOG)
    public ResponseEntity<String> createBlog(@RequestBody BlogRequestDto blogRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = userService.verifyToken(token);

        String userIdByUsername = userService.findUserIdByJwtToken(token);

        BlogEntity createdBlogEntity = BlogEntity.builder()
                .id(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .title(blogRequest.getTitle())
                .description(blogRequest.getDescription())
                .ownerId(userIdByUsername)
                .messages(new ArrayList<>())
                .build();

        blogService.save(createdBlogEntity);

        return ResponseEntity
                .ok("blog with id '%d' was created".formatted(createdBlogEntity.getId()));
    }

    @PutMapping(UPDATE_BLOG_BY_ID)
    public ResponseEntity<BlogResponseDto> updateBlogEntity(@PathVariable Long id, @RequestBody BlogRequestDto blogRequest) {
        BlogEntity foundBlogEntity = blogService.findById(id);

        blogService.updateBlogEntity(blogRequest, foundBlogEntity);

        blogService.save(foundBlogEntity);

        BlogResponseDto blogResponse = BlogResponseDto.builder()
                .title(foundBlogEntity.getTitle())
                .description(foundBlogEntity.getDescription())
                .messages(foundBlogEntity.getMessages())
                .build();

        return ResponseEntity
                .ok(blogResponse);
    }

    @DeleteMapping(DELETE_BLOG_BY_ID)
    public ResponseEntity<BlogResponseDto> deleteBlogById(@PathVariable Long id) {
        BlogEntity foundBlogEntity = blogService.findById(id);

        blogService.deleteById(id);

        BlogResponseDto response = BlogResponseDto.builder()
                .id(foundBlogEntity.getId())
                .title(foundBlogEntity.getTitle())
                .description(foundBlogEntity.getDescription())
                .ownerId(foundBlogEntity.getOwnerId())
                .messages(foundBlogEntity.getMessages())
                .build();

        return ResponseEntity
                .ok(response);
    }
}
