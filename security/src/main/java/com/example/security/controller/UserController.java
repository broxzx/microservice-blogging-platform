package com.example.security.controller;

import com.example.security.dto.UserDtoResponse;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final String GET_USER_BY_ID = "/{id}";
    private static final String GET_USER_BY_USERNAME = "/by-username";
    private static final String GET_USER_BY_JWT_TOKEN = "/by-jwt-token";


    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserResponseById(id));
    }

    @GetMapping(GET_USER_BY_USERNAME)
    public ResponseEntity<UserDtoResponse> getUserByUsername(@RequestParam("username") String username) {
        return ResponseEntity.ok(userService.getUserResponseByUsername(username));
    }

    @GetMapping(GET_USER_BY_JWT_TOKEN)
    public ResponseEntity<UserDtoResponse> getUserDtoResponseByJwtToken(@RequestParam("token") String token) {
        return ResponseEntity.ok(userService.getUserDtoResponseByJwtToken(token));
    }
}
