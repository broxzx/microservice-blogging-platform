package com.example.blogservice.service;

import com.example.blogservice.dto.BlogRequestDto;
import com.example.blogservice.entity.BlogEntity;
import com.example.blogservice.exception.BlogNotFoundException;
import com.example.blogservice.repository.BlogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Represents a service class for managing blog entities.
 */
@Service
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;

    public List<BlogEntity> findAll() {
        return blogRepository.findAll();
    }

    /**
     * Finds a BlogEntity object by its ID.
     *
     * @param id The ID of the blog entity.
     * @return The BlogEntity object with the specified ID.
     * @throws BlogNotFoundException If no blog entity is found with the specified ID.
     */
    public BlogEntity findById(Long id) {
        return blogRepository.findById(id).orElseThrow(
                () -> new BlogNotFoundException("blog with id '%d' was not found".formatted(id))
        );
    }

    /**
     * Saves a BlogEntity object.
     *
     * @param blogEntity The BlogEntity object to be saved.
     */
    public void save(BlogEntity blogEntity) {
        blogRepository.save(blogEntity);
    }

    /**
     * Updates the properties of a BlogEntity object.
     *
     * @param blogRequest The BlogRequestDto object containing the updated values for the blog entity.
     * @param blogEntity The BlogEntity object to be updated.
     */
    public void updateBlogEntity(BlogRequestDto blogRequest, BlogEntity blogEntity) {
        blogEntity.setTitle(blogRequest.getTitle());
        blogEntity.setDescription(blogRequest.getDescription());
    }

    /**
     * Deletes a BlogEntity object by its ID.
     *
     * @param id The ID of the blog entity to be deleted.
     */
    public void deleteById(Long id) {
        blogRepository.deleteById(id);
    }

    /**
     * Finds a list of BlogEntity objects by owner ID.
     *
     * @param userId The ID of the owner.
     * @return A list of BlogEntity objects with the specified owner ID.
     */
    public List<BlogEntity> findBlogEntityByOwnerId(String userId) {
        return blogRepository.findByOwnerId(userId);
    }
}
