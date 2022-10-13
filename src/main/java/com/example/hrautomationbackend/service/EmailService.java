package com.example.hrautomationbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    void sendEmail(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("bayliz@bk.ru");
        msg.setTo(email);
        msg.setSubject("Код авторизации");
        msg.setText("Ваш код атворизации: 1234");
        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
}