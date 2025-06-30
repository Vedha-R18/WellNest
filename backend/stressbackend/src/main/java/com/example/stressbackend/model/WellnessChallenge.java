package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "wellness_challenges")
public class WellnessChallenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String challengeType; // DAILY, WEEKLY, MONTHLY

    private int targetDays;
    private int pointsReward;
    private boolean isActive = true;

    @Column(name = "start_date")
    private Timestamp startDate;

    @Column(name = "end_date")
    private Timestamp endDate;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    // Explicit getter and setter for isActive to ensure they're available
    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }
} 