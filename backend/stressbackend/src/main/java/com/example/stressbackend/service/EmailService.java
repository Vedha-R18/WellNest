package com.example.stressbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendGroupActivity(List<String> emails, String activity) {
        for (String email : emails) {
            sendEmail(email, "Group Activity Assigned", activity);
        }
    }

    public void sendSingleActivity(String email, String activity) {
        System.out.println("Single activity email sent to " + email + ": " + activity);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}