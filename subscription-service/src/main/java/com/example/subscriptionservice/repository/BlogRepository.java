package com.example.subscriptionservice.repository;

import com.example.subscriptionservice.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<BlogEntity, Long> {
}
