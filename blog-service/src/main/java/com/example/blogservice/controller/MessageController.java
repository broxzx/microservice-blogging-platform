package com.example.blogservice.controller;

import com.example.blogservice.dto.MessageRequestDto;
import com.example.blogservice.dto.MessageResponseDto;
import com.example.blogservice.entity.BlogEntity;
import com.example.blogservice.entity.MessageEntity;
import com.example.blogservice.service.BlogService;
import com.example.blogservice.service.MessageService;
import com.example.blogservice.service.SequenceGeneratorService;
import com.example.blogservice.service.UserService;
import com.example.blogservice.utils.MessageResponseDtoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blog/{blogId}")
public class MessageController {

    private final BlogService blogService;

    private final MessageService messageService;

    private final MessageResponseDtoFactory messageResponseDtoFactory;

    private final SequenceGeneratorService sequenceGeneratorService;

    private final UserService userService;

    private static final String SEQUENCE_NAME = "message_sequence";

    private static final String GET_ALL_MESSAGES_BY_BLOG_ID = "/";
    private static final String GET_MESSAGE_BY_ID_BY_BLOG_ID = "/{messageId}";
    private static final String CREATE_MESSAGE_IN_BLOG = "/";
    private static final String UPDATE_MESSAGE_BY_ID_IN_BLOG = "/{messageId}";
    private static final String DELETE_MESSAGE_BY_ID_IN_BLOG = "/{messageId}";

    @GetMapping(GET_ALL_MESSAGES_BY_BLOG_ID)
    public ResponseEntity<List<MessageResponseDto>> getAllMessagesInBlog(@PathVariable Long blogId) {
        //check whether blog exists
        blogService.findById(blogId);

        List<MessageEntity> messageEntityById = messageService.findMessageEntitiesByBlogId(blogId);

        List<MessageResponseDto> response = messageEntityById
                .stream()
                .map(messageResponseDtoFactory::makeMessageResponseDto)
                .toList();

        return ResponseEntity
                .ok(response);
    }

    @GetMapping(GET_MESSAGE_BY_ID_BY_BLOG_ID)
    public ResponseEntity<MessageResponseDto> findMessageEntityById(@PathVariable Long blogId, @PathVariable Long messageId) {
        blogService.findById(blogId);

        MessageEntity messageEntityById = messageService.findMessageEntityById(messageId);

        MessageResponseDto response = messageResponseDtoFactory.makeMessageResponseDto(messageEntityById);

        return ResponseEntity
                .ok(response);
    }

    @PostMapping(CREATE_MESSAGE_IN_BLOG)
    public ResponseEntity<MessageResponseDto> createMessageEntity(@PathVariable Long blogId, @RequestBody MessageRequestDto messageRequest,
                                                                  @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        token = userService.verifyToken(token);
        String userId = userService.findUserIdByJwtToken(token);

        BlogEntity foundBlogEntity = blogService.findById(blogId);

        MessageEntity messageEntity = MessageEntity.builder()
                .id(sequenceGeneratorService.generateSequence(SEQUENCE_NAME))
                .content(messageRequest.getContent())
                .authorId(userId)
                .blogId(String.valueOf(blogId))
                .createdAt(LocalDateTime.now())
                .build();

        List<MessageEntity> messages = foundBlogEntity.getMessages();
        messages.add(messageEntity);
        foundBlogEntity.setMessages(messages);
        blogService.save(foundBlogEntity);
        messageService.save(messageEntity);

        MessageResponseDto response = messageResponseDtoFactory.makeMessageResponseDto(messageEntity);

        return ResponseEntity
                .ok(response);
    }

    @PutMapping(UPDATE_MESSAGE_BY_ID_IN_BLOG)
    public ResponseEntity<MessageResponseDto> updateMessageEntityById(@PathVariable Long blogId, @PathVariable Long messageId,
                                                                      @RequestBody MessageRequestDto messageRequest) {
        blogService.findById(blogId);

        MessageEntity foundMessageEntity = messageService.findMessageEntityById(messageId);

        messageService.updateMessageEntity(foundMessageEntity, messageRequest);
        messageService.save(foundMessageEntity);

        MessageResponseDto response = MessageResponseDto.builder()
                .id(foundMessageEntity.getId())
                .content(foundMessageEntity.getContent())
                .authorId(foundMessageEntity.getAuthorId())
                .blogId(foundMessageEntity.getBlogId())
                .createdAt(foundMessageEntity.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(DELETE_MESSAGE_BY_ID_IN_BLOG)
    public ResponseEntity<String> deleteMessageById(@PathVariable Long blogId, @PathVariable Long messageId) {
        BlogEntity foundBlogEntity = blogService.findById(blogId);

        foundBlogEntity.getMessages().removeIf(message -> Objects.equals(message.getId(), messageId));

        messageService.findMessageEntityById(messageId);
        messageService.deleteById(messageId);

        blogService.save(foundBlogEntity);

        return ResponseEntity.ok("message with id '%d' was deleted".formatted(messageId));
    }
}
