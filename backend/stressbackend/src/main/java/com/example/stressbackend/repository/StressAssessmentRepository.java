package com.example.stressbackend.repository;

import com.example.stressbackend.model.StressAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.sql.Timestamp;
import java.util.List;

public interface StressAssessmentRepository extends JpaRepository<StressAssessment, Long> {
    List<StressAssessment> findByUserIdAndCreatedAtAfter(Long userId, Timestamp after);
}

