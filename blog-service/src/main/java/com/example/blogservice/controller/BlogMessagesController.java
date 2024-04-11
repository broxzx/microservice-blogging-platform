package com.example.blogservice.controller;

import com.example.blogservice.model.MessageModelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("blog/messages/{blogId}")
@RequiredArgsConstructor
public class BlogMessagesController {

    private final WebClient webClient;

    private static final String GET_ALL_MESSAGES_BY_ID = "/";

    @GetMapping(GET_ALL_MESSAGES_BY_ID)
    public ResponseEntity<MessageModelResponse[]> getAllMessagesByBlogId(@PathVariable Long blogId) {
        MessageModelResponse[] response = webClient
                .get()
                .uri("http://localhost:8080/messages/blog/{blogId}", blogId)
                .retrieve()
                .bodyToMono(MessageModelResponse[].class)
                .block();

        return ResponseEntity.ok(response);
    }
}
