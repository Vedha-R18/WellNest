package com.example.stressbackend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_stats")
public class UserStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int totalPoints = 0;
    private int currentStreak = 0;
    private int longestStreak = 0;
    private int challengesCompleted = 0;
    private int checkInsCompleted = 0;
    private int moodLogsCompleted = 0;

    // Achievement badges
    private boolean firstCheckIn = false;
    private boolean weekStreak = false;
    private boolean monthStreak = false;
    private boolean challengeMaster = false;
    private boolean moodTracker = false;
    private boolean stressReducer = false;
} 