package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "mood_logs")
public class MoodLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String mood; // emoji representation: 😊 😔 😤 😴 😰 etc.

    private String notes;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
} 