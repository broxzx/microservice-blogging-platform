package com.example.blogservice.service;

import com.example.blogservice.dto.MessageRequestDto;
import com.example.blogservice.exception.MessageNotFoundException;
import com.example.blogservice.repository.MessageRepository;
import com.example.entityservice.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void save(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
    }

    public MessageEntity findMessageEntityById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(
                        () -> new MessageNotFoundException("message with id '%d' was not found".formatted(id))
                );
    }

    public List<MessageEntity> findMessageEntitiesByBlogId(Long blogId) {
        return messageRepository.findByBlogId(String.valueOf(blogId));
    }

    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    public void updateMessageEntity(MessageEntity message, MessageRequestDto request) {
        message.setContent(request.getContent());
    }
}
