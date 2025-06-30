package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "support_questions")
public class SupportQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy; // Support team or user who created the question

    private boolean isDaily = false; // true if uploaded as daily question

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // The user who asked the question (null for daily questions)

    @Column(columnDefinition = "TEXT")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "answered_by")
    private User answeredBy; // Support team member who answered

    @Column(name = "answered_at")
    private Timestamp answeredAt;
} 