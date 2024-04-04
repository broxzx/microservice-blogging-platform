package com.example.subscriptionservice.service;

import com.example.entityservice.entity.SubscriptionEntity;
import com.example.subscriptionservice.exception.SubscriptionNotFoundException;
import com.example.subscriptionservice.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public List<SubscriptionEntity> findAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public SubscriptionEntity getSubscriptionById(Long id) {
        return subscriptionRepository.findById(id)
                .orElseThrow(
                        () -> new SubscriptionNotFoundException("subscription with id %d was not found".formatted(id))
                );
    }

    public SubscriptionEntity subscribe(SubscriptionEntity subscriptionEntity) {
        return subscriptionRepository.save(subscriptionEntity);

    }

    public void deleteSubscriptionById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public List<SubscriptionEntity> findSubscriptionByUserId(String userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    public void deleteSubscriptionByUserIdAndBlogId(String userId, String blogId) {
        subscriptionRepository.deleteByUserIdAndBlogId(userId, blogId);
    }

    public List<SubscriptionEntity> findSubscriptionByBlogId(String blogId) {
        return subscriptionRepository.findByBlogId(blogId);
    }
}
