package com.example.blogservice.repository;

import com.example.entityservice.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BlogRepository extends MongoRepository<BlogEntity, Long> {

    List<BlogEntity> findByOwnerId(String ownerId);
}
