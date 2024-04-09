package com.example.subscriptionservice.service;

import com.example.subscriptionservice.exception.TokenIsInvalidException;
import com.example.subscriptionservice.model.BlogModelResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class BlogService {

    private final WebClient webClient;

    public BlogModelResponse getBlogEntityById(Long blogId) {
        BlogModelResponse blogModelResponse = webClient
                .get()
                .uri("http://localhost:8080/blog/{id}", blogId)
                .header(HttpHeaders.AUTHORIZATION, getUserToken())
                .retrieve()
                .bodyToMono(BlogModelResponse.class)
                .block();

        log.info(blogModelResponse);

        return blogModelResponse;
    }

    public String getUserToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null && header.startsWith("Bearer ")) {
            return header;
        } else {
            throw new TokenIsInvalidException("token is either invalid or absent");
        }
    }

}
