package com.example.stressbackend.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "group_activities")
public class GroupActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // Getters and setters
}
