package com.messageservice.messageservice.model;

/**
 * This class represents a response model for a blog.
 */
public record BlogModelResponse(Long id, String title, String description, String ownerId) {
}
