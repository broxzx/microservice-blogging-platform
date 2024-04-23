package com.example.blogservice.controller;

import com.example.blogservice.model.MessageModelResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

/**
 * Controller class for handling blog message requests.
 */
@RestController
@RequestMapping("blog/messages/{blogId}")
@RequiredArgsConstructor
public class BlogMessagesController {

    private final WebClient webClient;

    private final Tracer tracer;

    private static final String GET_ALL_MESSAGES_BY_ID = "/";

    @Value("${host.name}")
    private String host;

    /**
     * Retrieves all messages by blog ID.
     *
     * @param blogId The ID of the blog to retrieve messages for.
     * @return ResponseEntity<MessageModelResponse[]> A response entity containing an array of message models.
     */
    @CircuitBreaker(name = "blog")
    @Retry(name = "blog")
    @GetMapping(GET_ALL_MESSAGES_BY_ID)
    public ResponseEntity<MessageModelResponse[]> getAllMessagesByBlogId(@PathVariable Long blogId) {
        Span messageEntityLookUp = tracer.nextSpan().name("Message Entity LookUp");

        try (Tracer.SpanInScope ignored = tracer.withSpan(messageEntityLookUp.start())) {
            MessageModelResponse[] response = webClient
                    .get()
                    .uri("http://%s:8080/messages/blog/{blogId}".formatted(host), blogId)
                    .retrieve()
                    .bodyToMono(MessageModelResponse[].class)
                    .subscribeOn(Schedulers.boundedElastic())
                    .doOnEach(signal -> {
                        if (signal.hasError()) {
                            Objects.requireNonNull(tracer.currentSpan()).tag("error", Objects.requireNonNull(signal.getThrowable()).getMessage());
                        }
                    })
                    .block();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Objects.requireNonNull(tracer.currentSpan()).tag("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } finally {
            messageEntityLookUp.end();
        }
    }
}
