package com.example.security.controller;

import com.example.security.dto.UserDtoResponse;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The UserController class handles user-related HTTP requests and responses.
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final String GET_USER_BY_ID = "/{id}";
    private static final String GET_USER_BY_USERNAME = "/by-username";
    private static final String GET_USER_BY_JWT_TOKEN = "/by-jwt-token";
    private static final String GET_USER_BY_EMAIL = "/by-email";


    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user.
     * @return A ResponseEntity containing the UserDtoResponse as the response body.
     */
    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserResponseById(id));
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return A ResponseEntity containing the UserDtoResponse as the response body.
     */
    @GetMapping(GET_USER_BY_USERNAME)
    public ResponseEntity<UserDtoResponse> getUserByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.getUserResponseByUsername(username));
    }

    @GetMapping(GET_USER_BY_EMAIL)
    public ResponseEntity<UserDtoResponse> getUserByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    /**
     * Retrieves a UserDtoResponse by the given JWT token.
     *
     * @param token the JWT token provided as a query parameter
     * @return a ResponseEntity containing the UserDtoResponse retrieved from userService
     */
    @GetMapping(GET_USER_BY_JWT_TOKEN)
    @ConditionalOnProperty(prefix = "security", name = "jwt.enabled")
    public ResponseEntity<UserDtoResponse> getUserDtoResponseByJwtToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(userService.getUserDtoResponseByJwtToken(token));
    }
}
