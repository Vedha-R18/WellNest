package com.example.stressbackend.config;

import com.example.stressbackend.model.WellnessChallenge;
import com.example.stressbackend.repository.WellnessChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private WellnessChallengeRepository wellnessChallengeRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample wellness challenges if none exist
        if (wellnessChallengeRepository.count() == 0) {
            initializeSampleChallenges();
        }
    }

    private void initializeSampleChallenges() {
        // Daily Meditation Challenge
        WellnessChallenge meditationChallenge = new WellnessChallenge();
        meditationChallenge.setTitle("Daily Meditation Challenge");
        meditationChallenge.setDescription("Practice meditation for 10 minutes every day for 7 days. This will help reduce stress and improve mental clarity.");
        meditationChallenge.setChallengeType("DAILY");
        meditationChallenge.setTargetDays(7);
        meditationChallenge.setPointsReward(50);
        meditationChallenge.setIsActive(true);
        meditationChallenge.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        meditationChallenge.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)));
        wellnessChallengeRepository.save(meditationChallenge);

        // Walking Challenge
        WellnessChallenge walkingChallenge = new WellnessChallenge();
        walkingChallenge.setTitle("10,000 Steps Challenge");
        walkingChallenge.setDescription("Walk 10,000 steps every day for 5 days. Physical activity is great for stress relief and overall health.");
        walkingChallenge.setChallengeType("DAILY");
        walkingChallenge.setTargetDays(5);
        walkingChallenge.setPointsReward(40);
        walkingChallenge.setIsActive(true);
        walkingChallenge.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        walkingChallenge.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)));
        wellnessChallengeRepository.save(walkingChallenge);

        // Gratitude Challenge
        WellnessChallenge gratitudeChallenge = new WellnessChallenge();
        gratitudeChallenge.setTitle("Gratitude Journal Challenge");
        gratitudeChallenge.setDescription("Write down 3 things you're grateful for every day for 14 days. Practicing gratitude can significantly improve mental well-being.");
        gratitudeChallenge.setChallengeType("DAILY");
        gratitudeChallenge.setTargetDays(14);
        gratitudeChallenge.setPointsReward(70);
        gratitudeChallenge.setIsActive(true);
        gratitudeChallenge.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        gratitudeChallenge.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)));
        wellnessChallengeRepository.save(gratitudeChallenge);

        // Hydration Challenge
        WellnessChallenge hydrationChallenge = new WellnessChallenge();
        hydrationChallenge.setTitle("Stay Hydrated Challenge");
        hydrationChallenge.setDescription("Drink 8 glasses of water every day for 7 days. Proper hydration is essential for stress management and overall health.");
        hydrationChallenge.setChallengeType("DAILY");
        hydrationChallenge.setTargetDays(7);
        hydrationChallenge.setPointsReward(35);
        hydrationChallenge.setIsActive(true);
        hydrationChallenge.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        hydrationChallenge.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)));
        wellnessChallengeRepository.save(hydrationChallenge);

        // Deep Breathing Challenge
        WellnessChallenge breathingChallenge = new WellnessChallenge();
        breathingChallenge.setTitle("Deep Breathing Challenge");
        breathingChallenge.setDescription("Practice deep breathing exercises for 5 minutes twice a day for 10 days. This technique is proven to reduce stress and anxiety.");
        breathingChallenge.setChallengeType("DAILY");
        breathingChallenge.setTargetDays(10);
        breathingChallenge.setPointsReward(60);
        breathingChallenge.setIsActive(true);
        breathingChallenge.setStartDate(Timestamp.valueOf(LocalDateTime.now()));
        breathingChallenge.setEndDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)));
        wellnessChallengeRepository.save(breathingChallenge);

        System.out.println("Sample wellness challenges initialized successfully!");
    }
} 