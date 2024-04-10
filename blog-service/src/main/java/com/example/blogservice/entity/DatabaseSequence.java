package com.example.blogservice.entity;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "database_sequences")
@Data
public class DatabaseSequence {

    @Id
    private String id;

    private Long seq;
}