package com.example.stressbackend.controller;

import com.example.stressbackend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // Inject UserRepository, PasswordEncoder, JwtUtil, etc. as needed

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Registration logic here
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Login logic here
        return ResponseEntity.ok("Login successful");
    }
} 