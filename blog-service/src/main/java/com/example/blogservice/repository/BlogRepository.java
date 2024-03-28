package com.example.blogservice.repository;

import com.example.entityservice.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BlogRepository extends MongoRepository<BlogEntity, Long> {

}
