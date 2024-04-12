package com.messageservice.messageservice.aop;

import com.messageservice.messageservice.dto.MessageRequestDto;
import com.messageservice.messageservice.dto.MessageResponseDto;
import com.messageservice.messageservice.entity.MessageEntity;
import com.messageservice.messageservice.exception.AccessDeniedException;
import com.messageservice.messageservice.exception.TokenIsInvalidException;
import com.messageservice.messageservice.model.BlogModelResponse;
import com.messageservice.messageservice.model.UserModelResponse;
import com.messageservice.messageservice.service.MessageService;
import com.messageservice.messageservice.utils.Role;
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

@Aspect
@Component
@RequiredArgsConstructor
public class MessageFilterAspect {

    private final MessageService messageService;

    private final WebClient webClient;

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
                userModelResponse.role() != Role.ROLE_ADMIN &&
                !Objects.equals(blogModelResponse.ownerId(), String.valueOf(userModelResponse.userId()))) {
            throw new AccessDeniedException("you do not have access to this operation");
        }

        ResponseEntity<MessageResponseDto> response = (ResponseEntity<MessageResponseDto>) joinPoint.proceed();

        return response;
    }

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

    private String getAuthorizationHeader() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            return token;
        } else {
            throw new TokenIsInvalidException("token is either missing or invalid");
        }
    }

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
