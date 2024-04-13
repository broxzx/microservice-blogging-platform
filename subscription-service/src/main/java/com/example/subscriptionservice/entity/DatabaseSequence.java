package com.example.subscriptionservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a sequence generator for MongoDB.
 * The class is annotated with `@Document` to specify the collection name in MongoDB.
 */
@Document(collection = "database_sequences")
@Data
public class DatabaseSequence {
    @Id
    private String id;

    private Long seq;
}
