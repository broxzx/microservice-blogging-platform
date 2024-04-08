package com.example.blogservice.repository;

import com.example.blogservice.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<MessageEntity, Long> {
    List<MessageEntity> findByBlogId(String blogId);
}
