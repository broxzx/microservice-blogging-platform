package com.example.blogservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Document(collection = "blogs")
public class BlogEntity {

    @Transient
    public static final String SEQUENCE_NAME = "blog_sequence";

    @Id
    private Long id;

    private String title;

    private String description;

    private String ownerId;

//    private List<Long> messageId;

    private List<MessageEntity> messages;
}
