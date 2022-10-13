package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private CodeGenerationService codeService;

    void sendEmail(UserEntity user) {
        int auth_code = codeService.generateCode();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("arinkakogyt@gmail.com");
        msg.setTo(user.getEmail());
        msg.setSubject("Код авторизации");
        String text = "Здравствуйте, " + user.getUsername() + "!\nВаш код атворизации: " + auth_code;
        msg.setText(text);
        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
}