package com.example.stressbackend.model;

import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "single_activities")
public class SingleActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    // Getters and setters
}
