package com.example.stressbackend.repository;

import com.example.stressbackend.model.TeamActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface TeamActivityRepository extends JpaRepository<TeamActivity, Long> {
    List<TeamActivity> findByDepartment(String department);
    List<TeamActivity> findByIsCompletedFalse();
    
    @Query("SELECT ta FROM TeamActivity ta WHERE ta.scheduledDate >= :startDate AND ta.scheduledDate <= :endDate")
    List<TeamActivity> findByDateRange(@Param("startDate") Timestamp startDate, @Param("endDate") Timestamp endDate);
    
    @Query("SELECT ta FROM TeamActivity ta WHERE ta.department = :department AND ta.isCompleted = false")
    List<TeamActivity> findUpcomingByDepartment(@Param("department") String department);
} 