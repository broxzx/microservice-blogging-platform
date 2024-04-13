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

/**
 * The SubscriptionController class is responsible for handling HTTP requests related to subscriptions.
 */
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


    /**
     * Retrieves all the subscribers' usernames for a given blog by its ID.
     *
     * @param blogId The ID of the blog
     * @return ResponseEntity<List < String>> A list of usernames of the blog subscribers
     */
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

    /**
     * Retrieves all the subscriptions.
     *
     * @return List<SubscriptionEntity> A list of SubscriptionEntity objects representing the subscriptions
     */
    @GetMapping("/")
    public List<SubscriptionEntity> getAll() {
        return subscriptionService.findAllSubscriptions();
    }

    /**
     * Retrieves all the blog names with the given user ID.
     *
     * @param userId The ID of the user
     * @return ResponseEntity<List < String>> A list of blog names subscribed by the user
     */
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

    /**
     * Creates a new subscription based on the provided subscription request.
     *
     * @param subscriptionRequest The subscription request containing the blog and user IDs
     * @return ResponseEntity<SubscriptionResponse> The response entity containing the created subscription
     */
    @PostMapping(CREATE_SUBSCRIPTION)
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest subscriptionRequest) {
        SubscriptionEntity subscriptionEntity = subscriptionService.saveSubscriptionAndSendNotification(subscriptionRequest);

        SubscriptionResponse response = subscriptionResponseMapper.makeSubscriptionResponse(subscriptionEntity);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    /**
     * Updates a subscription based on the provided subscription ID and request.
     *
     * @param subscriptionId The ID of the subscription to update
     * @param request The subscription request containing the updated data
     * @return ResponseEntity<SubscriptionResponse> The response entity containing the updated subscription
     */
    @PutMapping(UPDATE_SUBSCRIPTION)
    public ResponseEntity<SubscriptionResponse> updateSubscription(@PathVariable Long subscriptionId, @RequestBody SubscriptionRequest request) {
        SubscriptionEntity foundSubscriptionById = subscriptionService.getSubscriptionById(subscriptionId);

        SubscriptionEntity updatedSubscriptionEntity = subscriptionService.updateSubscription(foundSubscriptionById, request);

        subscriptionService.saveSubscriptionAndSendNotification(request);

        SubscriptionResponse response = subscriptionResponseMapper.makeSubscriptionResponse(updatedSubscriptionEntity);

        return ResponseEntity
                .ok(response);
    }


    /**
     * Deletes a subscription by its ID.
     *
     * @param subscriptionId The ID of the subscription to delete
     * @return ResponseEntity<SubscriptionResponse> The response entity containing the deleted subscription
     */
    @DeleteMapping(DELETE_SUBSCRIPTION)
    public ResponseEntity<SubscriptionResponse> deleteSubscriptionById(@PathVariable Long subscriptionId) {
        SubscriptionEntity subscriptionEntity = subscriptionService.getSubscriptionById(subscriptionId);

        subscriptionService.deleteSubscriptionById(subscriptionId);

        return ResponseEntity
                .ok(subscriptionResponseMapper.makeSubscriptionResponse(subscriptionEntity));
    }
}
