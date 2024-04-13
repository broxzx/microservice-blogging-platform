package com.example.subscriptionservice.service;

import com.example.subscriptionservice.exception.TokenIsInvalidException;
import com.example.subscriptionservice.model.BlogModelResponse;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

/**
 * The BlogService class is responsible for retrieving a BlogModelResponse from a blog API by its ID and processing the user token.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class BlogService {

    private final WebClient webClient;

    private final Tracer tracer;

    /**
     * Retrieves a BlogModelResponse from a blog API by its ID.
     *
     * @param blogId The ID of the blog entity.
     * @return The retrieved BlogModelResponse object.
     */
    public BlogModelResponse getBlogEntityById(Long blogId) {
        Span blogEntityLookUp = tracer.nextSpan().name("Blog Entity LookUp");

        try (Tracer.SpanInScope ignored = tracer.withSpan(blogEntityLookUp.start())) {
            BlogModelResponse blogModelResponse = webClient
                    .get()
                    .uri("http://localhost:8080/blog/{id}", blogId)
                    .header(HttpHeaders.AUTHORIZATION, getUserToken())
                    .retrieve()
                    .bodyToMono(BlogModelResponse.class)
                    .subscribeOn(Schedulers.boundedElastic())
                    .doOnEach(signal -> {
                        if (signal.hasError()) {
                            Objects.requireNonNull(tracer.currentSpan()).tag("error", Objects.requireNonNull(signal.getThrowable()).getMessage());
                        }
                    })
                    .block();

            log.info(blogModelResponse);

            return blogModelResponse;
        } finally {
            blogEntityLookUp.end();
        }
    }

    /**
     * Retrieves the user token from the request's authorization header.
     *
     * @return The user token from the request's authorization header.
     * @throws TokenIsInvalidException If the token is either invalid or absent.
     */
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
