package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @Column(nullable = false)
    private String role; // EMPLOYEE, MANAGER, ADMIN

    private String department;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    // Getters and setters
} 