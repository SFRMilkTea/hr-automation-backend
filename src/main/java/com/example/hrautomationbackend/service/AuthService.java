package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private JwtProvider jwtProvider;

    public boolean sendCode(String email) throws UserNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с данным email не существует");
        } else {
            int authCode = codeService.generateCode();
            emailService.sendEmail(user, authCode);
            user.setAuthCode(authCode);
            final LocalDateTime expTime = LocalDateTime.now().plusMinutes(5);
            user.setCodeExpTime(expTime);
            userRepository.save(user);
            return true;
        }
    }

    public JwtResponse checkCode(String email, int code) throws WrongAuthorizationCodeException {
        UserEntity user = userRepository.findByEmail(email);
        if (user.getAuthCode() != code)
            throw new WrongAuthorizationCodeException("Неверный код авторизации");
        else {
            user.setAuthCode(-1);
            userRepository.save(user);
            return (jwtService.getTokens(user));
        }
    }
}
