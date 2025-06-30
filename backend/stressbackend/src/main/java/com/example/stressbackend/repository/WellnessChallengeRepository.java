package com.example.stressbackend.repository;

import com.example.stressbackend.model.WellnessChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WellnessChallengeRepository extends JpaRepository<WellnessChallenge, Long> {
    List<WellnessChallenge> findByIsActiveTrue();
    List<WellnessChallenge> findByChallengeType(String challengeType);
} 