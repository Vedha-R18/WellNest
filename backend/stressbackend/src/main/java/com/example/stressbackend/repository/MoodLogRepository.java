package com.example.stressbackend.repository;

import com.example.stressbackend.model.MoodLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface MoodLogRepository extends JpaRepository<MoodLog, Long> {
    List<MoodLog> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    @Query("SELECT m FROM MoodLog m WHERE m.user.id = :userId AND m.createdAt >= :startDate")
    List<MoodLog> findByUserIdAndDateRange(@Param("userId") Long userId, @Param("startDate") Timestamp startDate);
    
    @Query("SELECT m FROM MoodLog m WHERE m.user.department = :department AND m.createdAt >= :startDate")
    List<MoodLog> findByDepartmentAndDateRange(@Param("department") String department, @Param("startDate") Timestamp startDate);
} 