package com.messageservice.messageservice.service;

import com.messageservice.messageservice.dto.MessageRequestDto;
import com.messageservice.messageservice.entity.MessageEntity;
import com.messageservice.messageservice.exception.MessageNotFoundException;
import com.messageservice.messageservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The MessageService class provides methods for managing message entities.
 * It interacts with the MessageRepository to perform persistence operations.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    /**
     * Retrieves all messages from the system.
     *
     * @return a list of MessageEntity objects representing all messages
     */
    public List<MessageEntity> getAllMessages() {
        return messageRepository.findAll();
    }

    /**
     * Saves a message entity to the database.
     *
     * @param messageEntity The message entity to be saved.
     */
    public void save(MessageEntity messageEntity) {
        messageRepository.save(messageEntity);
    }

    /**
     * Retrieves a message entity by its ID.
     *
     * @param id The ID of the message entity.
     * @return The message entity with the specified ID.
     * @throws MessageNotFoundException if the message with the specified ID was not found.
     */
    public MessageEntity getMessageEntityById(Long id) {
        return messageRepository.findById(id)
                .orElseThrow(
                        () -> new MessageNotFoundException("message with id '%d' was not found".formatted(id))
                );
    }

    /**
     * Retrieves a list of MessageEntity objects by blogId.
     *
     * @param blogId The ID of the blog.
     * @return A list of MessageEntity objects with the specified blogId.
     */
    public List<MessageEntity> getMessageEntitiesByBlogId(Long blogId) {
        return messageRepository.findByBlogId(String.valueOf(blogId));
    }

    /**
     * Deletes a message entity by its ID.
     *
     * @param id The ID of the message entity to delete.
     */
    public void deleteById(Long id) {
        messageRepository.deleteById(id);
    }

    /**
     * Updates the content of a message entity with the provided message request DTO.
     *
     * @param message  The message entity to update.
     * @param request  The message request DTO containing the new message content.
     */
    public void updateMessageEntity(MessageEntity message, MessageRequestDto request) {
        message.setContent(request.getContent());
    }

    /**
     * Deletes message entities by blogId.
     * This method deletes all message entities associated with a specific blog identified by its blogId.
     *
     * @param blogId The ID of the blog.
     */
    public void deleteMessageEntityByBlogId(String blogId) {
        messageRepository.deleteByBlogId(blogId);
    }
}
