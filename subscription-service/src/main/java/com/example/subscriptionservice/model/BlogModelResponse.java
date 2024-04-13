package com.example.subscriptionservice.model;

/**
 * Represents a response object for the BlogModel.
 */
public record BlogModelResponse(Long id, String title, String description, String ownerId) {
}
