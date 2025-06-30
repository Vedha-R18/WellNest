package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "user_challenges")
public class UserChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private WellnessChallenge challenge;

    private int completedDays = 0;
    private boolean isCompleted = false;
    private int pointsEarned = 0;

    @Column(name = "started_at")
    private Timestamp startedAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "completed_at")
    private Timestamp completedAt;
} 