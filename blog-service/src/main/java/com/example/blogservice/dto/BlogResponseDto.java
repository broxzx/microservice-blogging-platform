package com.example.blogservice.dto;

import com.example.entityservice.entity.MessageEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BlogResponseDto {

    private String title;

    private String description;

    private List<MessageEntity> messages;
}
