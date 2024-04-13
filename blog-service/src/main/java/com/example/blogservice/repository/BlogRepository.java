package com.example.blogservice.repository;

import com.example.blogservice.entity.BlogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Represents a repository interface to perform CRUD operations on BlogEntity objects.
 */
public interface BlogRepository extends MongoRepository<BlogEntity, Long> {

    /**
     * Finds a list of BlogEntity objects by owner ID.
     *
     * @param ownerId the ID of the owner
     * @return a list of BlogEntity objects with the specified owner ID
     */
    List<BlogEntity> findByOwnerId(String ownerId);
}
