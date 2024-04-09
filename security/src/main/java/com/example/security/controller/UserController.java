package com.example.security.controller;

import com.example.security.dto.UserDtoResponse;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private static final String GET_USER_BY_ID = "/{id}";


    @GetMapping(GET_USER_BY_ID)
    public ResponseEntity<UserDtoResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserResponseById(id));
    }
}
