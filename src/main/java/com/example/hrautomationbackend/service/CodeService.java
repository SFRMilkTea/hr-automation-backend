package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.util.concurrent.TimeUnit.*;

@Service
public class CodeService {

    @Autowired
    private UserRepository userRepository;

    int generateCode() {
        int min = 1000;
        int max = 9999;
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }

    @Scheduled(timeUnit = SECONDS, fixedRate = 30)
    void deleteCode() {
        Iterable<UserEntity> users = userRepository.findAll();
        for (UserEntity user : users) {
            if (user.getCodeExpTime()==null) {
                user.setCodeExpTime(LocalDateTime.now().minusMinutes(30));
            }
            if (user.getCodeExpTime().compareTo(LocalDateTime.now()) < 0) {
                user.setAuthCode(-1);
                userRepository.save(user);
            }
        }
    }
}
