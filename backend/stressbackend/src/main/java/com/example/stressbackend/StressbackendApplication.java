package com.example.stressbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
public class StressbackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(StressbackendApplication.class, args);
    }

    @Scheduled(fixedRate = 60000)
    public void processAlerts() {
        System.out.println("Processing alerts...");
        // rest of your code
    }
}
