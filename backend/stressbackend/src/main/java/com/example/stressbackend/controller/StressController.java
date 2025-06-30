package com.example.stressbackend.controller;

import com.example.stressbackend.model.*;
import com.example.stressbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stress")
@CrossOrigin(origins = "http://localhost:3000")
public class StressController {

    @Autowired
    private StressAssessmentRepository stressAssessmentRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/submit")
    public ResponseEntity<?> submitStress(@RequestBody Map<String, Object> payload) {
        Long userId = Long.valueOf(payload.get("userId").toString());
        int score = Integer.parseInt(payload.get("score").toString());
        User user = userRepository.findById(userId).orElseThrow();
        StressAssessment assessment = new StressAssessment();
        assessment.setUser(user);
        assessment.setScore(score);
        stressAssessmentRepository.save(assessment);
        return ResponseEntity.ok("Your stress score was received: " + score);
    }

    // NEW: Trends endpoint
    @GetMapping("/trends/{userId}")
    public ResponseEntity<?> getStressTrends(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "7") int days) {
        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            Timestamp startTimestamp = Timestamp.valueOf(startDate);

            // You need this method in your repository!
            List<StressAssessment> assessments = stressAssessmentRepository.findByUserIdAndCreatedAtAfter(userId, startTimestamp);

            // Group by date and average the score for each day
            Map<String, Double> dateToAvgScore = assessments.stream()
                .collect(Collectors.groupingBy(
                    a -> a.getCreatedAt().toLocalDateTime().toLocalDate().toString(),
                    Collectors.averagingInt(StressAssessment::getScore)
                ));

            // Convert to list of {date, value}
            List<Map<String, Object>> trends = dateToAvgScore.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("date", e.getKey());
                    m.put("value", e.getValue());
                    return m;
                })
                .collect(Collectors.toList());

            return ResponseEntity.ok(trends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching stress trends: " + e.getMessage());
        }
    }
}