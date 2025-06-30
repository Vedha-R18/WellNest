package com.example.stressbackend.service;

import com.example.stressbackend.model.*;
import com.example.stressbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AlertProcessingService {
    @Autowired LowStressAlertRepository alertRepo;
    @Autowired GroupActivityRepository groupRepo;
    @Autowired SingleActivityRepository singleRepo;
    @Autowired EmailService emailService;
    @Autowired UserRepository userRepository;
    @Autowired SingleActivityRepository singleActivityRepo;

    @Scheduled(fixedRate = 60000)
    public void processAlerts() {
        System.out.println("Processing alerts...");
        List<LowStressAlert> alerts = alertRepo.findByProcessedFalse();
        for (LowStressAlert alert : alerts) {
            User user = alert.getUser();
            // Fetch a random single activity
            SingleActivity activity = singleActivityRepo.findRandomActivity();
            String activityMsg = activity != null ? activity.getDescription() : "Take a break and relax!";
            emailService.sendSingleActivity(user.getEmail(), "Your stress is low. Please try this activity: " + activityMsg);
            alert.setProcessed(true);
            alertRepo.save(alert);
        }
    }

    private GroupActivity getRandomGroupActivity() {
        List<GroupActivity> all = groupRepo.findAll();
        return all.get(new Random().nextInt(all.size()));
    }

    private SingleActivity getRandomSingleActivity() {
        List<SingleActivity> all = singleRepo.findAll();
        return all.get(new Random().nextInt(all.size()));
    }
}