package com.example.blogservice.aop;

import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.exception.AccessDenied;
import com.example.blogservice.service.BlogService;
import com.example.blogservice.service.UserService;
import com.example.entityservice.entity.BlogEntity;
import com.example.entityservice.entity.UserEntity;
import com.example.entityservice.role.Role;
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


    @Around("execution(* com.example.blogservice.controller.BlogController.getAllBlogs()) && @annotation(org.springframework.web.bind.annotation.GetMapping))")
    public Object filterBlogs(ProceedingJoinPoint joinPoint) throws Throwable {
        String userId = getUserIdByToken();
        UserEntity userEntity = getUserByToken();

        List<BlogEntity> filteredBlogEntities = blogService.findBlogEntityByOwnerId(userId);

        ResponseEntity<List<BlogResponseDto>> result = (ResponseEntity<List<BlogResponseDto>>) joinPoint.proceed();

        if (result.getBody() != null) {
            Role currentUserRole = userEntity.getRole();
            if (currentUserRole == Role.ADMIN || currentUserRole == Role.MANAGER) {
                return result;
            } else {
                List<BlogResponseDto> response = result.getBody()
                        .stream()
                        .filter(blog -> filteredBlogEntities.stream().anyMatch(userBlog -> Objects.equals(userBlog.getId(), blog.getId())))
                        .collect(Collectors.toList());

                return ResponseEntity.ok(response);
            }
        } else {
            return result;
        }
    }

    @Around("""
                    execution(* com.example.blogservice.controller.BlogController.getBlogById(Long)) && @annotation(org.springframework.web.bind.annotation.GetMapping)
            """)
    public Object checkAccessGetBlogById(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        UserEntity userEntity = getUserByToken();

        ResponseEntity<BlogResponseDto> response = (ResponseEntity<BlogResponseDto>) proceedingJoinPoint.proceed();

        if (response.getBody() != null) {
            Role currentUserRole = userEntity.getRole();

            if (currentUserRole == Role.ADMIN || currentUserRole == Role.MANAGER) {
                return response;
            }

            BlogResponseDto responseBody = response.getBody();

            if (!Objects.equals(responseBody.getOwnerId(), String.valueOf(userEntity.getId()))) {
                throw new AccessDenied("you don't have an access to this blog");
            } else {
                return response;
            }

        } else {
            return response;
        }
    }

    @Around("""
                    execution(* com.example.blogservice.controller.BlogController.updateBlogEntity(Long, com.example.blogservice.dto.BlogRequestDto))
                     && args(id, com.example.blogservice.dto.BlogRequestDto) && @annotation(org.springframework.web.bind.annotation.PutMapping) ||
                    execution(* com.example.blogservice.controller.BlogController.deleteBlogById(Long)) && args(id) && @annotation(org.springframework.web.bind.annotation.DeleteMapping)
            """)
    public Object checkAccessUpdateDeleteBlogEntity(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        UserEntity userEntity = getUserByToken();

        BlogEntity foundBlogEntity = blogService.findById(id);

        if (!Objects.equals(foundBlogEntity.getOwnerId(), String.valueOf(userEntity.getId())) && (userEntity.getRole() != Role.ADMIN && userEntity.getRole() != Role.MANAGER)) {
            throw new AccessDenied("you don't have an access to the blog with id %d".formatted(id));
        }

        return (ResponseEntity<BlogResponseDto>) joinPoint.proceed();
    }

    public String getUserIdByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        token = userService.verifyToken(token);

        return userService.findUserIdByJwtToken(token);
    }

    public UserEntity getUserByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        token = userService.verifyToken(token);

        return userService.findUserEntityByJwtToken(token);
    }
}
