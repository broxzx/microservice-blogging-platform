package com.example.blogservice.aop;

import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.exception.AccessForbidden;
import com.example.blogservice.service.BlogService;
import com.example.blogservice.service.UserService;
import com.example.entityservice.entity.BlogEntity;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class BlogFilterAspect {

    private final BlogService blogService;

    private final UserService userService;


    @Around("execution(* com.example.blogservice.controller.BlogController.getAllBlogs(..))")
    public Object filterBlogs(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = userIdByToken();

        List<BlogEntity> filteredBlogEntities = blogService.findBlogEntityByOwnerId(userId);

        ResponseEntity<List<BlogResponseDto>> result = (ResponseEntity<List<BlogResponseDto>>) joinPoint.proceed();

        if (result.getBody() != null) {
            List<BlogResponseDto> response = result.getBody()
                    .stream()
                    .filter(blog -> filteredBlogEntities.stream().anyMatch(userBlog -> Objects.equals(userBlog.getId(), blog.getId())))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } else {
            return result;
        }
    }

    @Around("execution(* com.example.blogservice.controller.BlogController.getBlogById(..))")
    public Object filterBlog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String userId = userIdByToken();

        ResponseEntity<BlogResponseDto> response = (ResponseEntity<BlogResponseDto>) proceedingJoinPoint.proceed();

        if (response.getBody() != null) {
            BlogResponseDto responseBody = response.getBody();

            if (!Objects.equals(responseBody.getOwnerId(), userId)) {
                throw new AccessForbidden("you don't have an access to this blog");
            } else {
                return response;
            }

        } else {
            return response;
        }
    }

    public String userIdByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        token = userService.verifyToken(token);

        return userService.findUserIdByJwtToken(token);
    }
}
