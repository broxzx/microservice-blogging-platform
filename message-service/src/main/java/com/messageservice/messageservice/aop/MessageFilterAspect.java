package com.messageservice.messageservice.aop;

import com.messageservice.messageservice.dto.MessageRequestDto;
import com.messageservice.messageservice.dto.MessageResponseDto;
import com.messageservice.messageservice.entity.MessageEntity;
import com.messageservice.messageservice.exception.AccessDeniedException;
import com.messageservice.messageservice.exception.TokenIsInvalidException;
import com.messageservice.messageservice.model.BlogModelResponse;
import com.messageservice.messageservice.model.UserModelResponse;
import com.messageservice.messageservice.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

/**
 * Aspect class for filtering messages based on access control.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class MessageFilterAspect {

    private final MessageService messageService;

    private final WebClient webClient;

    /**
     * Checks access to delete a message entity by ID.
     *
     * @param joinPoint The join point of the method.
     * @param messageId The ID of the message entity to delete.
     * @return The response entity containing the deleted message response DTO.
     * @throws Throwable Throws an AccessDeniedException if the user does not have access to delete the message.
     */
    @Around(value = """
            execution(* com.messageservice.messageservice.controller.MessageController.deleteMessageEntityById(Long))
            && @annotation(org.springframework.web.bind.annotation.DeleteMapping)
            && args(messageId)
            """, argNames = "joinPoint,messageId")
    public Object checkAccessToDeleteMessage(ProceedingJoinPoint joinPoint, Long messageId) throws Throwable {
        MessageEntity messageEntity = messageService.getMessageEntityById(messageId);

        String token = getAuthorizationHeader();
        UserModelResponse userModelResponse = getUserModelResponseByToken(token);
        BlogModelResponse blogModelResponse = getBlogModelResponseById(messageEntity.getBlogId());

        if (!Objects.equals(messageEntity.getAuthorId(), String.valueOf(userModelResponse.userId())) &&
                !Objects.equals(blogModelResponse.ownerId(), String.valueOf(userModelResponse.userId()))) {
            throw new AccessDeniedException("you do not have access to this operation");
        }

        ResponseEntity<MessageResponseDto> response = (ResponseEntity<MessageResponseDto>) joinPoint.proceed();

        return response;
    }

    /**
     * Checks if the user has access to update a message entity.
     *
     * @param joinPoint         The join point of the method.
     * @param messageId         The ID of the message entity to update.
     * @param messageRequestDto The updated message request DTO.
     * @return The response entity containing the updated message response DTO.
     * @throws Throwable Throws an AccessDeniedException if the user does not have access to update the message.
     */
    @Around(value = """
            execution(* com.messageservice.messageservice.controller.MessageController
            .updateMessageEntity(Long, com.messageservice.messageservice.dto.MessageRequestDto))
            && @annotation(org.springframework.web.bind.annotation.PutMapping)
            && args(messageId, messageRequestDto)
            """, argNames = "joinPoint, messageId, messageRequestDto")
    public Object checkUpdateMessageAccess(ProceedingJoinPoint joinPoint, Long messageId, MessageRequestDto messageRequestDto) throws Throwable {
        MessageEntity messageEntity = messageService.getMessageEntityById(messageId);

        String token = getAuthorizationHeader();
        UserModelResponse userModelResponse = getUserModelResponseByToken(token);

        if (!Objects.equals(messageEntity.getAuthorId(), String.valueOf(userModelResponse.userId()))) {
            throw new AccessDeniedException("you do not have access to this operation");
        }

        ResponseEntity<MessageResponseDto> response = (ResponseEntity<MessageResponseDto>) joinPoint.proceed();

        return response;
    }

    /**
     * Retrieves a UserModelResponse by token.
     *
     * @param token The token used to retrieve the UserModelResponse.
     * @return The UserModelResponse retrieved from the server.
     */
    private UserModelResponse getUserModelResponseByToken(String token) {
        String userToken = token.substring(7);

        UserModelResponse response = webClient
                .get()
                .uri("http://localhost:8080/user/by-jwt-token", uriBuilder -> uriBuilder
                        .queryParam("token", userToken)
                        .build())
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader())
                .retrieve()
                .bodyToMono(UserModelResponse.class)
                .block();

        return response;
    }

    /**
     * Retrieves the authorization header from the current HTTP request.
     *
     * @return The authorization header as a string.
     * @throws TokenIsInvalidException if the token is missing or invalid.
     */
    private String getAuthorizationHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        System.out.println(token);

        if (token != null && token.startsWith("Bearer ")) {
            return token;
        } else {
            throw new TokenIsInvalidException("token is either missing or invalid");
        }
    }

    /**
     * Returns a BlogModelResponse object by its id.
     *
     * @param id The id of the blog model response to retrieve.
     * @return The BlogModelResponse object corresponding to the given id.
     */
    private BlogModelResponse getBlogModelResponseById(String id) {
        BlogModelResponse response = webClient
                .get()
                .uri("http://localhost:8080/blog/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader())
                .retrieve()
                .bodyToMono(BlogModelResponse.class)
                .block();

        return response;
    }
}
