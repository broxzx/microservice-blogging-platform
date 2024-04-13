package com.messageservice.messageservice.utils;

import com.messageservice.messageservice.dto.MessageResponseDto;
import com.messageservice.messageservice.entity.MessageEntity;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for creating MessageResponseDto objects based on MessageEntity objects.
 * It takes a MessageEntity object as input and creates a MessageResponseDto object with the corresponding values.
 */
@Component
public class MessageResponseDtoFactory {

    /**
     * Creates a MessageResponseDto object based on a given MessageEntity object.
     *
     * @param messageEntity The MessageEntity object to create the MessageResponseDto from.
     * @return The created MessageResponseDto object.
     */
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
