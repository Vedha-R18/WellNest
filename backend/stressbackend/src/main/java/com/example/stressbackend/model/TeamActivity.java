package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "team_activities")
public class TeamActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String activityType; // TEAM_BUILDING, WELLNESS_SESSION, SOCIAL_EVENT, TRAINING

    private String department;
    private String organizer; // Manager or HR person

    @Column(name = "scheduled_date")
    private Timestamp scheduledDate;

    private int maxParticipants;
    private boolean isMandatory = false;
    private boolean isCompleted = false;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
} 