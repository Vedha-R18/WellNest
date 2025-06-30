package com.example.stressbackend.controller;

import com.example.stressbackend.model.*;
import com.example.stressbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/challenges")
@CrossOrigin(origins = "http://localhost:3000")
public class ChallengeController {

    @Autowired
    private WellnessChallengeRepository wellnessChallengeRepository;

    @Autowired
    private UserChallengeRepository userChallengeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatsRepository userStatsRepository;

    @GetMapping("/active")
    public ResponseEntity<?> getActiveChallenges() {
        try {
            List<WellnessChallenge> challenges = wellnessChallengeRepository.findByIsActiveTrue();
            return ResponseEntity.ok(challenges);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching active challenges: " + e.getMessage());
        }
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinChallenge(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            Long challengeId = Long.valueOf(payload.get("challengeId").toString());

            User user = userRepository.findById(userId).orElseThrow();
            WellnessChallenge challenge = wellnessChallengeRepository.findById(challengeId).orElseThrow();

            // Check if user already joined this challenge
            List<UserChallenge> existingChallenges = userChallengeRepository.findByUserId(userId);
            boolean alreadyJoined = existingChallenges.stream()
                    .anyMatch(uc -> uc.getChallenge().getId().equals(challengeId));

            if (alreadyJoined) {
                return ResponseEntity.badRequest().body("User already joined this challenge");
            }

            UserChallenge userChallenge = new UserChallenge();
            userChallenge.setUser(user);
            userChallenge.setChallenge(challenge);
            userChallengeRepository.save(userChallenge);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Successfully joined challenge: " + challenge.getTitle());
            response.put("challengeId", challengeId);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error joining challenge: " + e.getMessage());
        }
    }

    @PostMapping("/update-progress")
    public ResponseEntity<?> updateChallengeProgress(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            Long challengeId = Long.valueOf(payload.get("challengeId").toString());
            int completedDays = Integer.parseInt(payload.get("completedDays").toString());

            UserChallenge userChallenge = userChallengeRepository.findByUserId(userId).stream()
                    .filter(uc -> uc.getChallenge().getId().equals(challengeId))
                    .findFirst()
                    .orElseThrow();

            userChallenge.setCompletedDays(completedDays);

            // Check if challenge is completed
            if (completedDays >= userChallenge.getChallenge().getTargetDays() && !userChallenge.isCompleted()) {
                userChallenge.setCompleted(true);
                userChallenge.setCompletedAt(new Timestamp(System.currentTimeMillis()));
                userChallenge.setPointsEarned(userChallenge.getChallenge().getPointsReward());

                // Update user stats
                Optional<UserStats> userStatsOpt = userStatsRepository.findByUserId(userId);
                UserStats userStats = userStatsOpt.orElseGet(() -> {
                    UserStats newStats = new UserStats();
                    newStats.setUser(userChallenge.getUser());
                    return newStats;
                });

                userStats.setTotalPoints(userStats.getTotalPoints() + userChallenge.getChallenge().getPointsReward());
                userStats.setChallengesCompleted(userStats.getChallengesCompleted() + 1);
                userStatsRepository.save(userStats);
            }

            userChallengeRepository.save(userChallenge);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Challenge progress updated");
            response.put("completedDays", completedDays);
            response.put("isCompleted", userChallenge.isCompleted());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating challenge progress: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserChallenges(@PathVariable Long userId) {
        try {
            List<UserChallenge> userChallenges = userChallengeRepository.findByUserId(userId);
            return ResponseEntity.ok(userChallenges);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching user challenges: " + e.getMessage());
        }
    }

    @GetMapping("/leaderboard")
    public ResponseEntity<?> getLeaderboard(@RequestParam(required = false) String department) {
        try {
            List<UserStats> leaderboard;
            if (department != null && !department.isEmpty()) {
                leaderboard = userStatsRepository.findByDepartmentOrderByPoints(department);
            } else {
                leaderboard = userStatsRepository.findAllOrderByPoints();
            }

            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching leaderboard: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    public ResponseEntity<?> createChallenge(@RequestBody Map<String, Object> payload) {
        try {
            WellnessChallenge challenge = new WellnessChallenge();
            challenge.setTitle(payload.get("title").toString());
            challenge.setDescription(payload.get("description").toString());
            challenge.setChallengeType(payload.get("challengeType").toString());
            challenge.setTargetDays(Integer.parseInt(payload.get("targetDays").toString()));
            challenge.setPointsReward(Integer.parseInt(payload.get("pointsReward").toString()));
            challenge.setIsActive(true);

            wellnessChallengeRepository.save(challenge);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Challenge created successfully");
            response.put("challengeId", challenge.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating challenge: " + e.getMessage());
        }
    }
} 