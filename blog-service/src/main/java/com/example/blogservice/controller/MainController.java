package com.example.blogservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("blog")
public class MainController {

    @GetMapping("/test")
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok("Hello from main controller (blog-service)");
    }
}
