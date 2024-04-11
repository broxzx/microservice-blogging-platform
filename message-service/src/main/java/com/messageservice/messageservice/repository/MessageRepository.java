package com.messageservice.messageservice.repository;

import com.messageservice.messageservice.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<MessageEntity, Long> {
    List<MessageEntity> findByBlogId(String blogId);
}
