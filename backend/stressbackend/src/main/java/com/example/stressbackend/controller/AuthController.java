package com.example.stressbackend.controller;

import com.example.stressbackend.model.User;
import com.example.stressbackend.service.UserService;
import com.example.stressbackend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Check if email is company email (optional: add your domain check here)
        if (!user.getEmail().endsWith("@yourcompany.com")) {
            return ResponseEntity.badRequest().body("Please use your company email.");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> found = userService.findByEmail(user.getEmail());
        if (found.isEmpty() || !passwordEncoder.matches(user.getPassword(), found.get().getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtUtil.generateToken(found.get().getEmail());
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", found.get().getRole());
        response.put("userId", found.get().getId());
        return ResponseEntity.ok(response);
    }
}