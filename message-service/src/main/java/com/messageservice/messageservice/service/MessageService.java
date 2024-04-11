package com.messageservice.messageservice.service;

import com.messageservice.messageservice.dto.MessageRequestDto;
import com.messageservice.messageservice.entity.MessageEntity;
import com.messageservice.messageservice.exception.MessageNotFoundException;
import com.messageservice.messageservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    public void save(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
    }

    public MessageEntity getMessageEntityById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(
                        () -> new MessageNotFoundException("message with id '%d' was not found".formatted(id))
                );
    }

    public List<MessageEntity> getMessageEntitiesByBlogId(Long blogId) {
        return messageRepository.findByBlogId(String.valueOf(blogId));
    }

    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    public void updateMessageEntity(MessageEntity message, MessageRequestDto request) {
        message.setContent(request.getContent());
    }
}
