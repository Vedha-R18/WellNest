package com.example.stressbackend.repository;

import com.example.stressbackend.model.SupportQuestion;
import com.example.stressbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupportQuestionRepository extends JpaRepository<SupportQuestion, Long> {
    @Query("SELECT q FROM SupportQuestion q WHERE q.isDaily = true ORDER BY q.createdAt DESC")
    List<SupportQuestion> findDailyQuestions();

    @Query("SELECT q FROM SupportQuestion q WHERE q.user.id = :userId ORDER BY q.createdAt DESC")
    List<SupportQuestion> findByUserId(@Param("userId") Long userId);

    @Query("SELECT q FROM SupportQuestion q WHERE q.answer IS NULL AND q.isDaily = false ORDER BY q.createdAt ASC")
    List<SupportQuestion> findPendingUserQuestions();

    @Query("SELECT COUNT(q) FROM SupportQuestion q WHERE q.isDaily = true AND DATE(q.createdAt) = CURRENT_DATE")
    int countTodayDailyQuestions();
} 