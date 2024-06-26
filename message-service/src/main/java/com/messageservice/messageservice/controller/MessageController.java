package com.messageservice.messageservice.controller;

import com.messageservice.messageservice.dto.MessageRequestDto;
import com.messageservice.messageservice.dto.MessageResponseDto;
import com.messageservice.messageservice.entity.MessageEntity;
import com.messageservice.messageservice.model.UserModelResponse;
import com.messageservice.messageservice.service.MessageService;
import com.messageservice.messageservice.service.SequenceGeneratorService;
import com.messageservice.messageservice.service.UserService;
import com.messageservice.messageservice.utils.MessageResponseDtoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Rest Controller for managing messages.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("messages")
public class MessageController {

    private final MessageService messageService;

    private final MessageResponseDtoFactory messageResponseDtoFactory;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final UserService userService;

    private static final String SEQUENCE_NAME = "message_sequence";

    private static final String GET_ALL_MESSAGES_BY_BLOG_ID = "/blog/{blogId}";
    private static final String GET_MESSAGE_BY_ID = "/{messageId}";
    private static final String CREATE_MESSAGE_IN_BLOG = "/";
    private static final String GET_ALL_MESSAGES = "/";
    private static final String UPDATE_MESSAGE_BY_ID = "/{messageId}";
    private static final String DELETE_MESSAGE_BY_ID = "/{messageId}";


    /**
     * Retrieves all messages in a blog.
     *
     * @param blogId The ID of the blog.
     * @return ResponseEntity containing a list of MessageResponseDto objects.
     */
    @GetMapping(GET_ALL_MESSAGES_BY_BLOG_ID)
    public ResponseEntity<List<MessageResponseDto>> getAllMessagesInBlog(@PathVariable Long blogId) {
        List<MessageEntity> messageEntityById = messageService.getMessageEntitiesByBlogId(blogId);

        List<MessageResponseDto> response = messageEntityById
                .stream()
                .map(messageResponseDtoFactory::makeMessageResponseDto)
                .toList();

        return ResponseEntity
                .ok(response);
    }

    /**
     * Retrieves a message entity by its ID.
     *
     * @param messageId The ID of the message entity.
     * @return ResponseEntity containing the MessageResponseDto of the message entity.
     */
    @GetMapping(GET_MESSAGE_BY_ID)
    public ResponseEntity<MessageResponseDto> findMessageEntityById(@PathVariable Long messageId) {
        MessageEntity messageEntityById = messageService.getMessageEntityById(messageId);

        MessageResponseDto response = messageResponseDtoFactory.makeMessageResponseDto(messageEntityById);

        return ResponseEntity
                .ok(response);
    }

    /**
     * Retrieves all messages in the system.
     *
     * @return ResponseEntity containing a list of MessageResponseDto objects.
     */
    @GetMapping(GET_ALL_MESSAGES)
    public ResponseEntity<List<MessageResponseDto>> getAllMessages() {
        List<MessageEntity> allMessages = messageService.getAllMessages();

        List<MessageResponseDto> response = allMessages
                .stream()
                .map(messageResponseDtoFactory::makeMessageResponseDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new message entity in the blog.
     *
     * @param messageRequest The request object containing the message content and blog ID.
     * @param token          The authorization token.
     * @return ResponseEntity containing the created MessageResponseDto.
     */
    @PostMapping(CREATE_MESSAGE_IN_BLOG)
    public ResponseEntity<MessageResponseDto> createMessageEntity(@RequestBody MessageRequestDto messageRequest,
                                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        UserModelResponse userModel = userService.getUserModelByToken(token);

        MessageEntity messageEntity = MessageEntity.builder()
                .id(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .content(messageRequest.getContent())
                .authorId(String.valueOf(userModel.userId()))
                .blogId(String.valueOf(messageRequest.getBlogId()))
                .createdAt(LocalDateTime.now())
                .build();

        messageService.save(messageEntity);

        MessageResponseDto response = messageResponseDtoFactory.makeMessageResponseDto(messageEntity);

        return ResponseEntity
                .ok(response);
    }

    /**
     * Updates a message entity with the specified ID using the provided message request DTO.
     *
     * @param messageId         The ID of the message entity to update.
     * @param messageRequestDto The message request DTO containing the updated message data.
     * @return A ResponseEntity containing MessageResponseDto representing the updated message entity.
     */
    @PutMapping(UPDATE_MESSAGE_BY_ID)
    public ResponseEntity<MessageResponseDto> updateMessageEntity(@PathVariable Long messageId,
                                                                  @RequestBody MessageRequestDto messageRequestDto) {
        MessageEntity messageEntityById = messageService.getMessageEntityById(messageId);

        messageService.updateMessageEntity(messageEntityById, messageRequestDto);

        messageService.save(messageEntityById);

        return ResponseEntity
                .ok(messageResponseDtoFactory.makeMessageResponseDto(messageEntityById));
    }

    /**
     * Deletes a message entity by its ID.
     *
     * @param messageId The ID of the message entity to delete.
     * @return A ResponseEntity containing the MessageResponseDto of the deleted message entity.
     */
    @DeleteMapping(DELETE_MESSAGE_BY_ID)
    public ResponseEntity<MessageResponseDto> deleteMessageEntityById(@PathVariable Long messageId) {
        MessageEntity messageEntity = messageService.getMessageEntityById(messageId);

        messageService.deleteById(messageId);

        return ResponseEntity.ok(messageResponseDtoFactory.makeMessageResponseDto(messageEntity));
    }

}
