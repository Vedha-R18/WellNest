package com.example.stressbackend.controller;

import com.example.stressbackend.model.MoodLog;
import com.example.stressbackend.model.User;
import com.example.stressbackend.repository.MoodLogRepository;
import com.example.stressbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mood")
@CrossOrigin(origins = "http://localhost:3000")
public class MoodController {

    @Autowired
    private MoodLogRepository moodLogRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/log")
    public ResponseEntity<?> logMood(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            String mood = payload.get("mood").toString();
            String notes = payload.get("notes") != null ? payload.get("notes").toString() : "";

            User user = userRepository.findById(userId).orElseThrow();
            
            MoodLog moodLog = new MoodLog();
            moodLog.setUser(user);
            moodLog.setMood(mood);
            moodLog.setNotes(notes);
            
            moodLogRepository.save(moodLog);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Mood logged successfully");
            response.put("mood", mood);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error logging mood: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMoodHistory(@PathVariable Long userId) {
        try {
            List<MoodLog> moodLogs = moodLogRepository.findByUserIdOrderByCreatedAtDesc(userId);
            return ResponseEntity.ok(moodLogs);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching mood history: " + e.getMessage());
        }
    }

    @GetMapping("/trends/{userId}")
    public ResponseEntity<?> getMoodTrends(@PathVariable Long userId, 
                                         @RequestParam(defaultValue = "7") int days) {
        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            Timestamp startTimestamp = Timestamp.valueOf(startDate);
            
            List<MoodLog> moodLogs = moodLogRepository.findByUserIdAndDateRange(userId, startTimestamp);
            
            // Calculate mood trends
            Map<String, Object> trends = new HashMap<>();
            Map<String, Integer> moodCounts = new HashMap<>();
            
            for (MoodLog log : moodLogs) {
                moodCounts.put(log.getMood(), moodCounts.getOrDefault(log.getMood(), 0) + 1);
            }
            
            trends.put("moodCounts", moodCounts);
            trends.put("totalLogs", moodLogs.size());
            trends.put("period", days + " days");
            
            return ResponseEntity.ok(trends);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching mood trends: " + e.getMessage());
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<?> getDepartmentMoodOverview(@PathVariable String department,
                                                      @RequestParam(defaultValue = "7") int days) {
        try {
            LocalDateTime startDate = LocalDateTime.now().minusDays(days);
            Timestamp startTimestamp = Timestamp.valueOf(startDate);
            
            List<MoodLog> moodLogs = moodLogRepository.findByDepartmentAndDateRange(department, startTimestamp);
            
            // Calculate department mood overview
            Map<String, Object> overview = new HashMap<>();
            Map<String, Integer> moodCounts = new HashMap<>();
            
            for (MoodLog log : moodLogs) {
                moodCounts.put(log.getMood(), moodCounts.getOrDefault(log.getMood(), 0) + 1);
            }
            
            overview.put("department", department);
            overview.put("moodCounts", moodCounts);
            overview.put("totalLogs", moodLogs.size());
            overview.put("period", days + " days");
            
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching department mood overview: " + e.getMessage());
        }
    }
} 