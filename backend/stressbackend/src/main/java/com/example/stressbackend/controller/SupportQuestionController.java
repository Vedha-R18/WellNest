package com.example.stressbackend.controller;

import com.example.stressbackend.model.SupportQuestion;
import com.example.stressbackend.model.User;
import com.example.stressbackend.repository.SupportQuestionRepository;
import com.example.stressbackend.repository.UserRepository;
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
public class SupportQuestionController {
    @Autowired
    private SupportQuestionRepository supportQuestionRepository;
    @Autowired
    private UserRepository userRepository;

    // 1. Support team uploads 10 daily questions
    @PostMapping("/daily-questions")
    public ResponseEntity<?> uploadDailyQuestions(@RequestBody List<Map<String, String>> questions, @RequestParam Long supportUserId) {
        int todayCount = supportQuestionRepository.countTodayDailyQuestions();
        if (todayCount + questions.size() > 10) {
            return ResponseEntity.badRequest().body("You can only add up to 10 daily questions per day.");
        }
        User supportUser = userRepository.findById(supportUserId).orElseThrow();
        for (Map<String, String> q : questions) {
            SupportQuestion sq = new SupportQuestion();
            sq.setQuestion(q.get("question"));
            sq.setCreatedBy(supportUser);
            sq.setIsDaily(true);
            supportQuestionRepository.save(sq);
        }
        return ResponseEntity.ok("Daily questions uploaded");
    }

    // 2. Users fetch daily questions
    @GetMapping("/daily-questions")
    public ResponseEntity<?> getDailyQuestions() {
        List<SupportQuestion> questions = supportQuestionRepository.findDailyQuestions();
        return ResponseEntity.ok(questions);
    }

    // 3. User submits a support question
    @PostMapping("/user-question")
    public ResponseEntity<?> submitUserQuestion(@RequestBody Map<String, String> payload) {
        Long userId = Long.valueOf(payload.get("userId"));
        String question = payload.get("question");
        User user = userRepository.findById(userId).orElseThrow();
        SupportQuestion sq = new SupportQuestion();
        sq.setQuestion(question);
        sq.setUser(user);
        sq.setCreatedBy(user);
        sq.setIsDaily(false);
        supportQuestionRepository.save(sq);
        return ResponseEntity.ok("Support question submitted");
    }

    // 4. User fetches their Q&A
    @GetMapping("/user-questions/{userId}")
    public ResponseEntity<?> getUserQuestions(@PathVariable Long userId) {
        List<SupportQuestion> questions = supportQuestionRepository.findByUserId(userId);
        return ResponseEntity.ok(questions);
    }

    // 5. Support team fetches unanswered user questions
    @GetMapping("/pending-questions")
    public ResponseEntity<?> getPendingUserQuestions() {
        List<SupportQuestion> questions = supportQuestionRepository.findPendingUserQuestions();
        return ResponseEntity.ok(questions);
    }

    // 6. Support team answers a user question
    @PostMapping("/answer")
    public ResponseEntity<?> answerUserQuestion(@RequestBody Map<String, String> payload) {
        Long questionId = Long.valueOf(payload.get("questionId"));
        String answer = payload.get("answer");
        Long supportUserId = Long.valueOf(payload.get("supportUserId"));
        SupportQuestion sq = supportQuestionRepository.findById(questionId).orElseThrow();
        User supportUser = userRepository.findById(supportUserId).orElseThrow();
        sq.setAnswer(answer);
        sq.setAnsweredBy(supportUser);
        sq.setAnsweredAt(new Timestamp(System.currentTimeMillis()));
        supportQuestionRepository.save(sq);
        return ResponseEntity.ok("Answer submitted");
    }
} 