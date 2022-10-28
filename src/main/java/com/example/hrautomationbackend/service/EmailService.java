package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    void sendEmail(UserEntity user, int code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("arinkakogyt@gmail.com");
        msg.setTo(user.getEmail());
        msg.setSubject("Код авторизации");
        String text = "Здравствуйте, " + user.getUsername() + "!\nВаш код атворизации: " + code;
        msg.setText(text);
        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}