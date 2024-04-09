package com.example.blogservice.aop;

import com.example.blogservice.dto.MessageRequestDto;
import com.example.blogservice.dto.MessageResponseDto;
import com.example.blogservice.entity.BlogEntity;
import com.example.blogservice.entity.MessageEntity;
import com.example.blogservice.entity.Role;
import com.example.blogservice.exception.AccessDeniedException;
import com.example.blogservice.model.UserModelResponse;
import com.example.blogservice.service.BlogService;
import com.example.blogservice.service.MessageService;
import com.example.blogservice.service.UserService;
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

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class MessageFilterAspect {

    private final UserService userService;

    private final BlogService blogService;

    private final MessageService messageService;

    @Around(value = """
                    execution(* com.example.blogservice.controller.MessageController.deleteMessageById(Long, Long)) && args(blogId, messageId)
                    && @annotation(org.springframework.web.bind.annotation.DeleteMapping)
            """, argNames = "joinPoint,blogId,messageId")
    public Object checkAccessToDeleteMessage(ProceedingJoinPoint joinPoint, Long blogId, Long messageId) throws Throwable {
        UserModelResponse userModelResponse = findUserEntityByToken();
        BlogEntity foundBlogEntity = blogService.findById(blogId);
        MessageEntity foundMessageEntity = messageService.findMessageEntityById(messageId);

        String stringUserId = String.valueOf(userModelResponse.userId());
        if (!Objects.equals(foundMessageEntity.getAuthorId(), stringUserId) && !Objects.equals(foundBlogEntity.getOwnerId(), stringUserId)
                && (userModelResponse.role() != Role.ROLE_ADMIN && userModelResponse.role() != Role.ROLE_MANAGER)) {
            throw new AccessDeniedException("you don't have access to this message");
        }

        ResponseEntity<String> response = (ResponseEntity<String>) joinPoint.proceed();

        return response;
    }

    @Around(value = """
                    execution(* com.example.blogservice.controller.MessageController.updateMessageEntityById(Long, Long, com.example.blogservice.dto.MessageRequestDto))
                    && args(blogId, messageId, messageRequestDto) && @annotation(org.springframework.web.bind.annotation.PutMapping)
            """, argNames = "joinPoint,blogId,messageId,messageRequestDto")
    public Object checkAccessUpdateMessage(ProceedingJoinPoint joinPoint, Long blogId, Long messageId, MessageRequestDto messageRequestDto) throws Throwable {
        UserModelResponse userModelResponse = findUserEntityByToken();
        MessageEntity foundMessageEntity = messageService.findMessageEntityById(messageId);

        String stringUserId = String.valueOf(userModelResponse.userId());

        if (!Objects.equals(foundMessageEntity.getAuthorId(), stringUserId) && userModelResponse.role() != Role.ROLE_ADMIN
                && userModelResponse.role() != Role.ROLE_MANAGER) {
            throw new AccessDeniedException("you are not owner of this message");
        }

        ResponseEntity<MessageResponseDto> response = (ResponseEntity<MessageResponseDto>) joinPoint.proceed();

        return response;
    }

    private UserModelResponse findUserEntityByToken() {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        token = userService.verifyToken(token);

        return userService.findUserEntityByJwtToken(token);
    }
}
