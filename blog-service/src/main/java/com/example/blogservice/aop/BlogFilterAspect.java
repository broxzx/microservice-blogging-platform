package com.example.blogservice.aop;

import com.example.blogservice.dto.BlogResponseDto;
import com.example.blogservice.entity.BlogEntity;
import com.example.blogservice.entity.Role;
import com.example.blogservice.exception.AccessDeniedException;
import com.example.blogservice.model.UserModelResponse;
import com.example.blogservice.service.BlogService;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
@RequiredArgsConstructor
public class BlogFilterAspect {

    private final BlogService blogService;

    private final UserService userService;


    @Around("""
                execution(* com.example.blogservice.controller.BlogController.getAllBlogs()) && @annotation(org.springframework.web.bind.annotation.GetMapping))
            """)
    public Object filterBlogs(ProceedingJoinPoint joinPoint) throws Throwable {
        UserModelResponse userModelResponse = getUserByToken();

        List<BlogEntity> filteredBlogEntities = blogService.findBlogEntityByOwnerId(String.valueOf(userModelResponse.userId()));

        ResponseEntity<List<BlogResponseDto>> result = (ResponseEntity<List<BlogResponseDto>>) joinPoint.proceed();

        if (result.getBody() != null) {
            Role currentUserRole = userModelResponse.role();
            if (currentUserRole == Role.ROLE_ADMIN || currentUserRole == Role.ROLE_MANAGER) {
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
        UserModelResponse userModelResponse = getUserByToken();

        ResponseEntity<BlogResponseDto> response = (ResponseEntity<BlogResponseDto>) proceedingJoinPoint.proceed();

        if (response.getBody() != null) {
            Role currentUserRole = userModelResponse.role();

            if (currentUserRole == Role.ROLE_ADMIN || currentUserRole == Role.ROLE_MANAGER) {
                return response;
            }

            BlogResponseDto responseBody = response.getBody();

            if (!Objects.equals(responseBody.getOwnerId(), String.valueOf(userModelResponse.userId()))) {
                throw new AccessDeniedException("you don't have access to this blog");
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
        UserModelResponse userModelResponse = getUserByToken();

        BlogEntity foundBlogEntity = blogService.findById(id);

        if (!Objects.equals(foundBlogEntity.getOwnerId(), String.valueOf(userModelResponse.userId()))
                && (userModelResponse.role() != Role.ROLE_ADMIN
                && userModelResponse.role() != Role.ROLE_MANAGER)) {
            throw new AccessDeniedException("you don't have access to the blog with id %d".formatted(id));
        }

        return (ResponseEntity<BlogResponseDto>) joinPoint.proceed();
    }

    private UserModelResponse getUserByToken() {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        token = userService.verifyToken(token);

        return userService.findUserEntityByJwtToken(token);
    }
}
