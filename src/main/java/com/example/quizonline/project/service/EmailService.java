package com.example.quizonline.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


@Service
public class EmailService {

    @Autowired private JavaMailSender mailSender;
    @Value("${spring.mail.username}") 
    private String from;

 // Simple text email 

     public void sendEmail(String to, String subject, String message) { 
     SimpleMailMessage mailMessage = new SimpleMailMessage();
      mailMessage.setFrom(from); 
      mailMessage.setTo(to);
      mailMessage.setSubject(subject);
      mailMessage.setText(message);
      mailSender.send(mailMessage);
 }
} 