package com.example.subscriptionservice.controller;

import com.example.entityservice.entity.SubscriptionEntity;
import com.example.subscriptionservice.repository.BlogRepository;
import com.example.subscriptionservice.repository.UserRepository;
import com.example.subscriptionservice.service.SequenceGeneratorService;
import com.example.subscriptionservice.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final BlogRepository blogRepository;

    private final UserRepository userRepository;

    private static final String FIND_ALL_SUBSCRIBERS = "/";
    private static final String FIND_ALL_USERS_SUBSCRIPTION = "/";

    private static final String CREATE_SUBSCRIPTION = "/";

    @GetMapping(FIND_ALL_SUBSCRIBERS)
    public ResponseEntity<List<String>> findAllBlogSubscribersByBlogId(@RequestParam(name = "blogId") Long blogId) {
        List<SubscriptionEntity> subscriptionsByBlogId = subscriptionService.findSubscriptionByBlogId(blogId);

        List<String> usernameSubscribers = subscriptionsByBlogId
                .stream()
                .map(SubscriptionEntity::getUsername)
                .toList();

        return ResponseEntity
                .ok(usernameSubscribers);
    }

    @GetMapping(FIND_ALL_USERS_SUBSCRIPTION)
    public ResponseEntity<List<String>> findAllBlogsWithUserId(@RequestParam(name = "userId") Long userId) {
        List<SubscriptionEntity> subscriptionsByUserId = subscriptionService.findSubscriptionByUserId(userId);

        List<String> userSubscription = subscriptionsByUserId
                .stream()
                .map(SubscriptionEntity::getBlogName)
                .toList();

        return ResponseEntity
                .ok(userSubscription);
    }

//    @PostMapping(CREATE_SUBSCRIPTION)
//    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
//        Optional<BlogEntity> byId = blogRepository.findById(subscriptionRequest.getBlogId());
//
//    }
}
