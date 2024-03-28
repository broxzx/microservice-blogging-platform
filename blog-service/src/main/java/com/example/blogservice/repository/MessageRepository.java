package com.example.blogservice.repository;

import com.example.entityservice.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<MessageEntity, Long> {

}
