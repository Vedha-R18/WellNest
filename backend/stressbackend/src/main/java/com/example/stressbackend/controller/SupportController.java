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

@RestController
@RequestMapping("/api/support")
@CrossOrigin(origins = "http://localhost:3000")
public class SupportController {

    @Autowired
    private SupportRequestRepository supportRequestRepository;

    @Autowired
    private TeamActivityRepository teamActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StressAssessmentRepository stressAssessmentRepository;

    @PostMapping("/request")
    public ResponseEntity<?> createSupportRequest(@RequestBody Map<String, Object> payload) {
        try {
            Long userId = Long.valueOf(payload.get("userId").toString());
            String requestType = payload.get("requestType").toString();
            String description = payload.get("description").toString();
            String priority = payload.get("priority").toString();

            User user = userRepository.findById(userId).orElseThrow();

            SupportRequest request = new SupportRequest();
            request.setUser(user);
            request.setRequestType(requestType);
            request.setDescription(description);
            request.setPriority(priority);
            request.setStatus("PENDING");

            supportRequestRepository.save(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Support request created successfully");
            response.put("requestId", request.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating support request: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserSupportRequests(@PathVariable Long userId) {
        try {
            List<SupportRequest> requests = supportRequestRepository.findByUserId(userId);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching support requests: " + e.getMessage());
        }
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<?> getDepartmentSupportRequests(@PathVariable String department) {
        try {
            List<SupportRequest> requests = supportRequestRepository.findByDepartment(department);
            return ResponseEntity.ok(requests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching department support requests: " + e.getMessage());
        }
    }

    @PutMapping("/{requestId}/status")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long requestId, 
                                               @RequestBody Map<String, Object> payload) {
        try {
            String status = payload.get("status").toString();
            String assignedTo = payload.get("assignedTo") != null ? payload.get("assignedTo").toString() : null;

            SupportRequest request = supportRequestRepository.findById(requestId).orElseThrow();
            request.setStatus(status);
            
            if (assignedTo != null) {
                request.setAssignedTo(assignedTo);
            }

            if (status.equals("RESOLVED")) {
                request.setResolvedAt(new Timestamp(System.currentTimeMillis()));
            }

            supportRequestRepository.save(request);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Support request status updated");
            response.put("status", status);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating request status: " + e.getMessage());
        }
    }

    @GetMapping("/critical")
    public ResponseEntity<?> getCriticalRequests() {
        try {
            List<SupportRequest> criticalRequests = supportRequestRepository.findCriticalRequests();
            return ResponseEntity.ok(criticalRequests);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching critical requests: " + e.getMessage());
        }
    }

    @PostMapping("/team-activity")
    public ResponseEntity<?> createTeamActivity(@RequestBody Map<String, Object> payload) {
        try {
            String title = payload.get("title").toString();
            String description = payload.get("description").toString();
            String activityType = payload.get("activityType").toString();
            String department = payload.get("department").toString();
            String organizer = payload.get("organizer").toString();
            String scheduledDateStr = payload.get("scheduledDate").toString();
            int maxParticipants = Integer.parseInt(payload.get("maxParticipants").toString());
            boolean isMandatory = Boolean.parseBoolean(payload.get("isMandatory").toString());

            TeamActivity activity = new TeamActivity();
            activity.setTitle(title);
            activity.setDescription(description);
            activity.setActivityType(activityType);
            activity.setDepartment(department);
            activity.setOrganizer(organizer);
            activity.setScheduledDate(Timestamp.valueOf(scheduledDateStr));
            activity.setMaxParticipants(maxParticipants);
            activity.setMandatory(isMandatory);

            teamActivityRepository.save(activity);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Team activity created successfully");
            response.put("activityId", activity.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating team activity: " + e.getMessage());
        }
    }

    @GetMapping("/team-activities/{department}")
    public ResponseEntity<?> getDepartmentTeamActivities(@PathVariable String department) {
        try {
            List<TeamActivity> activities = teamActivityRepository.findUpcomingByDepartment(department);
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error fetching team activities: " + e.getMessage());
        }
    }

    @GetMapping("/intervention-suggestions/{department}")
    public ResponseEntity<?> getInterventionSuggestions(@PathVariable String department) {
        try {
            // Get recent stress assessments for the department
            List<StressAssessment> recentAssessments = stressAssessmentRepository.findAll();
            
            // Calculate average stress level
            double avgStress = recentAssessments.stream()
                    .mapToInt(StressAssessment::getScore)
                    .average()
                    .orElse(0.0);

            Map<String, Object> suggestions = new HashMap<>();
            suggestions.put("department", department);
            suggestions.put("averageStressLevel", avgStress);

            // Generate suggestions based on stress level
            if (avgStress > 35) {
                suggestions.put("suggestion", "High stress detected. Consider organizing a team wellness session or stress management workshop.");
                suggestions.put("priority", "HIGH");
                suggestions.put("recommendedActivities", List.of("Stress Management Workshop", "Team Building Activity", "Mental Health Check-in"));
            } else if (avgStress > 25) {
                suggestions.put("suggestion", "Moderate stress levels. Consider a light team activity or wellness check-in.");
                suggestions.put("priority", "MEDIUM");
                suggestions.put("recommendedActivities", List.of("Team Lunch", "Wellness Check-in", "Light Exercise Session"));
            } else {
                suggestions.put("suggestion", "Stress levels are manageable. Continue with regular wellness activities.");
                suggestions.put("priority", "LOW");
                suggestions.put("recommendedActivities", List.of("Regular Check-ins", "Wellness Tips", "Team Recognition"));
            }

            return ResponseEntity.ok(suggestions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating intervention suggestions: " + e.getMessage());
        }
    }

    @GetMapping("/pending-requests")
    public ResponseEntity<?> getPendingSupportRequests() {
        List<SupportRequest> requests = supportRequestRepository.findByStatus("PENDING");
        return ResponseEntity.ok(requests);
    }
} 