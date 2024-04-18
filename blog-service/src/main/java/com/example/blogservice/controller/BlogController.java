package com.example.blogservice.controller;

import com.example.blogservice.dto.BlogRequestDto;
import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.entity.BlogEntity;
import com.example.blogservice.producer.RabbitMQProducer;
import com.example.blogservice.service.BlogService;
import com.example.blogservice.service.SequenceGeneratorService;
import com.example.blogservice.service.UserService;
import com.example.blogservice.utils.BlogResponseDtoFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The BlogController class handles the REST API endpoints for managing blog resources.
 */
@RestController
@RequestMapping("blog")
@RequiredArgsConstructor
@Log4j2
public class BlogController {

    private final BlogResponseDtoFactory blogResponseDtoFactory;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final BlogService blogService;

    private final UserService userService;

    private final RabbitMQProducer rabbitMQProducer;

    private static final String SEQUENCE_NAME = "blog_sequence";
    private static final String GET_ALL_BLOGS = "/";
    private static final String GET_BLOG_BY_ID = "/{id}";
    private static final String CREATE_BLOG = "/";
    private static final String UPDATE_BLOG_BY_ID = "/{id}";
    private static final String DELETE_BLOG_BY_ID = "/{id}";


    /**
     * Retrieves all blogs from the system.
     *
     * @return A ResponseEntity object containing the list of BlogResponseDto objects.
     * The response has a status code of 200 (OK) if the blogs are successfully retrieved.
     * Otherwise, an error response is returned.
     */
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

    /**
     * Retrieves a blog by its ID.
     *
     * @param id the ID of the blog to retrieve
     * @return a ResponseEntity containing the BlogResponseDto representing the retrieved blog
     */
    @GetMapping(GET_BLOG_BY_ID)
    public ResponseEntity<BlogResponseDto> getBlogById(@PathVariable Long id) {
        BlogEntity foundBlogEntity = blogService.findById(id);

        BlogResponseDto response = blogResponseDtoFactory.makeBlogResponseDto(foundBlogEntity);

        log.info(response);

        return ResponseEntity
                .ok(response);
    }

    /**
     * Creates a new blog.
     *
     * @param blogRequest the object representing the blog to be created
     * @param token       the authorization token
     * @return the response entity containing the created blog
     */
    @PostMapping(CREATE_BLOG)
    @CircuitBreaker(name = "blog", fallbackMethod = "blogFallbackMethod")
    @Retry(name = "blog")
    public ResponseEntity<BlogResponseDto> createBlog(@RequestBody BlogRequestDto blogRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = userService.verifyToken(token);

        String userIdByUsername = userService.findUserIdByJwtToken(token);

        BlogEntity createdBlogEntity = BlogEntity.builder()
                .id(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .title(blogRequest.getTitle())
                .description(blogRequest.getDescription())
                .ownerId(userIdByUsername)
                .build();

        blogService.save(createdBlogEntity);

        BlogResponseDto response = blogResponseDtoFactory.makeBlogResponseDto(createdBlogEntity);

        return ResponseEntity
                .ok(response);
    }

    /**
     * Fallback method for creating a blog. It is called when there is an exception during the creation process.
     *
     * @param blogRequest the request object containing the details of the blog to be created
     * @param token       the authorization token provided in the request header
     * @param e           the exception that occurred while creating the blog
     * @return the response entity with an error message indicating that the blog creation is unavailable at the moment
     */
    public ResponseEntity<BlogResponseDto> blogFallbackMethod(@RequestBody BlogRequestDto blogRequest, @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
                                                              RuntimeException e) {
        BlogResponseDto response = BlogResponseDto.builder()
                .description("Unable to create blog at this time. Please try again later.")
                .build();

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(response);
    }

    /**
     * Updates a blog entity with the provided blog request data.
     *
     * @param id          The ID of the blog entity to update.
     * @param blogRequest The blog request data to update the entity.
     * @return The updated blog response DTO wrapped in a ResponseEntity.
     */
    @PutMapping(UPDATE_BLOG_BY_ID)
    public ResponseEntity<BlogResponseDto> updateBlogEntity(@PathVariable Long id, @RequestBody BlogRequestDto blogRequest) {
        BlogEntity foundBlogEntity = blogService.findById(id);

        blogService.updateBlogEntity(blogRequest, foundBlogEntity);

        blogService.save(foundBlogEntity);

        BlogResponseDto blogResponse = blogResponseDtoFactory.makeBlogResponseDto(foundBlogEntity);

        return ResponseEntity
                .ok(blogResponse);
    }

    /**
     * Deletes a blog by its ID.
     *
     * @param id the ID of the blog to delete
     * @return a ResponseEntity with the BlogResponseDto of the deleted blog
     */
    @DeleteMapping(DELETE_BLOG_BY_ID)
    public ResponseEntity<BlogResponseDto> deleteBlogById(@PathVariable Long id) {
        BlogEntity foundBlogEntity = blogService.findById(id);

        blogService.deleteById(id);

        rabbitMQProducer.sendToDeleteMessage(String.valueOf(id));

        BlogResponseDto response = blogResponseDtoFactory.makeBlogResponseDto(foundBlogEntity);

        return ResponseEntity
                .ok(response);
    }
}
