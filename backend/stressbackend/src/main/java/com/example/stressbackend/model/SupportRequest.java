package com.example.stressbackend.model;

import jakarta.persistence.*;
import java.sql.Timestamp;
import lombok.Data;

@Data
@Entity
@Table(name = "support_requests")
public class SupportRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String requestType; // ANONYMOUS, MANAGER, HR, PEER

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String priority; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(nullable = false)
    private String status; // PENDING, IN_PROGRESS, RESOLVED, ESCALATED

    private String assignedTo; // Manager or HR person assigned

    @Column(name = "created_at")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "resolved_at")
    private Timestamp resolvedAt;
} 