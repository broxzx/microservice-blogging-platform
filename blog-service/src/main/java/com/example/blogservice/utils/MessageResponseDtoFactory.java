package com.example.blogservice.utils;

import com.example.blogservice.dto.MessageResponseDto;
import com.example.entityservice.entity.MessageEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageResponseDtoFactory {

    public MessageResponseDto makeMessageResponseDto(MessageEntity messageEntity) {
        return MessageResponseDto.builder()
                .id(messageEntity.getId())
                .content(messageEntity.getContent())
                .authorId(messageEntity.getAuthorId())
                .blogId(messageEntity.getBlogId())
                .createdAt(messageEntity.getCreatedAt())
                .build();
    }
}
