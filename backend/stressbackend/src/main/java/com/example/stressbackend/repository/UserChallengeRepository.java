package com.example.stressbackend.repository;

import com.example.stressbackend.model.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    List<UserChallenge> findByUserId(Long userId);
    List<UserChallenge> findByUserIdAndIsCompletedTrue(Long userId);
    
    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.department = :department AND uc.isCompleted = true")
    List<UserChallenge> findByDepartmentAndCompleted(@Param("department") String department);
} 