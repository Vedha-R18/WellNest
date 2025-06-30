package com.example.stressbackend.repository;

import com.example.stressbackend.model.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    Optional<UserStats> findByUserId(Long userId);
    
    @Query("SELECT us FROM UserStats us WHERE us.user.department = :department ORDER BY us.totalPoints DESC")
    List<UserStats> findByDepartmentOrderByPoints(@Param("department") String department);
    
    @Query("SELECT us FROM UserStats us ORDER BY us.totalPoints DESC")
    List<UserStats> findAllOrderByPoints();
} 