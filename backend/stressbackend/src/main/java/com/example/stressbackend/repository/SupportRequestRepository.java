package com.example.stressbackend.repository;

import com.example.stressbackend.model.SupportRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {
    List<SupportRequest> findByUserId(Long userId);
    List<SupportRequest> findByStatus(String status);
    List<SupportRequest> findByPriority(String priority);
    
    @Query("SELECT sr FROM SupportRequest sr WHERE sr.user.department = :department")
    List<SupportRequest> findByDepartment(@Param("department") String department);
    
    @Query("SELECT sr FROM SupportRequest sr WHERE sr.priority = 'CRITICAL' AND sr.status != 'RESOLVED'")
    List<SupportRequest> findCriticalRequests();
} 