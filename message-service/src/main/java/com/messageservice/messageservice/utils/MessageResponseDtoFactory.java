package com.messageservice.messageservice.utils;

import com.messageservice.messageservice.dto.MessageResponseDto;
import com.messageservice.messageservice.entity.MessageEntity;
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
