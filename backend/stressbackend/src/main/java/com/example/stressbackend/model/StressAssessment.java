package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;
@Data
@Entity
@Table(name = "stress_assessment")
public class StressAssessment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int score;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    // Getters and setters
}