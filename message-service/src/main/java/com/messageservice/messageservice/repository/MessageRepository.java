package com.messageservice.messageservice.repository;

import com.messageservice.messageservice.entity.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * The MessageRepository interface extends the MongoRepository interface to provide persistence operations for MessageEntity objects in a MongoDB database.
 * It defines a method to retrieve a list of MessageEntity objects by blogId.
 */
public interface MessageRepository extends MongoRepository<MessageEntity, Long> {
    /**
     * Finds a list of MessageEntity objects by blogId.
     *
     * @param blogId the ID of the blog
     * @return a list of MessageEntity objects
     */
    List<MessageEntity> findByBlogId(String blogId);

    void deleteByBlogId(String blogId);
}
