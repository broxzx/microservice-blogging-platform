package com.example.subscriptionservice.controller;

import com.example.subscriptionservice.dto.SubscriptionRequest;
import com.example.subscriptionservice.dto.SubscriptionResponse;
import com.example.subscriptionservice.entity.SubscriptionEntity;
import com.example.subscriptionservice.service.SubscriptionService;
import com.example.subscriptionservice.utils.SubscriptionResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    private final SubscriptionResponseMapper subscriptionResponseMapper;

    private static final String FIND_ALL_SUBSCRIBERS = "/subscribers/{blogId}";
    private static final String FIND_ALL_USERS_SUBSCRIPTION = "/subscriptions/{userId}";
    private static final String CREATE_SUBSCRIPTION = "/";
    private static final String UPDATE_SUBSCRIPTION = "/{subscriptionId}";
    private static final String DELETE_SUBSCRIPTION = "/{subscriptionId}";


    @GetMapping(FIND_ALL_SUBSCRIBERS)
    public ResponseEntity<List<String>> findAllBlogSubscribersByBlogId(@PathVariable Long blogId) {
        List<SubscriptionEntity> subscriptionsByBlogId = subscriptionService.findSubscriptionByBlogId(blogId);

        List<String> usernameSubscribers = subscriptionsByBlogId
                .stream()
                .map(SubscriptionEntity::getUsername)
                .toList();

        return ResponseEntity
                .ok(usernameSubscribers);
    }

    @GetMapping("/")
    public List<SubscriptionEntity> getAll() {
        return subscriptionService.findAllSubscriptions();
    }

    @GetMapping(FIND_ALL_USERS_SUBSCRIPTION)
    public ResponseEntity<List<String>> findAllBlogsWithUserId(@PathVariable Long userId) {
        List<SubscriptionEntity> subscriptionsByUserId = subscriptionService.findSubscriptionByUserId(userId);

        List<String> userSubscription = subscriptionsByUserId
                .stream()
                .map(SubscriptionEntity::getBlogName)
                .toList();

        return ResponseEntity
                .ok(userSubscription);
    }

    @PostMapping(CREATE_SUBSCRIPTION)
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
        SubscriptionEntity subscriptionEntity = subscriptionService.saveSubscriptionAndSendNotification(subscriptionRequest);

        SubscriptionResponse response = subscriptionResponseMapper.makeSubscriptionResponse(subscriptionEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PutMapping(UPDATE_SUBSCRIPTION)
    public ResponseEntity<SubscriptionResponse> updateSubscription(@PathVariable Long subscriptionId, @RequestBody SubscriptionRequest request) {
        SubscriptionEntity foundSubscriptionById = subscriptionService.getSubscriptionById(subscriptionId);

        SubscriptionEntity updatedSubscriptionEntity = subscriptionService.updateSubscription(foundSubscriptionById, request);

        subscriptionService.saveSubscriptionAndSendNotification(request);

        SubscriptionResponse response = subscriptionResponseMapper.makeSubscriptionResponse(updatedSubscriptionEntity);

        return ResponseEntity
                .ok(response);
    }


    @DeleteMapping(DELETE_SUBSCRIPTION)
    public ResponseEntity<SubscriptionResponse> deleteSubscriptionById(@PathVariable Long subscriptionId) {
        SubscriptionEntity subscriptionEntity = subscriptionService.getSubscriptionById(subscriptionId);

        subscriptionService.deleteSubscriptionById(subscriptionId);

        return ResponseEntity
                .ok(subscriptionResponseMapper.makeSubscriptionResponse(subscriptionEntity));
    }
}
