package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserNotAdminException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final CodeService codeService;

    public AuthService(UserRepository userRepository, EmailService emailService, JwtService jwtService,
                       CodeService codeService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.jwtService = jwtService;
        this.codeService = codeService;
    }

    public void checkAdminEmail(String email) throws UserNotFoundException, UserNotAdminException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с email " + email + " не существует");
        }
        if (!user.isAdmin()) {
            throw new UserNotAdminException("Недостаточно прав доступа");
        }
        sendCode(user);
    }

    public void checkEmail(String email) throws UserNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с email " + email + " не существует");
        }
        sendCode(user);
    }

    @Transactional
    public void sendCode(UserEntity user) {
        int authCode = codeService.generateCode();
        emailService.sendEmail(user, authCode);
        user.setAuthCode(authCode);
        final LocalDateTime expTime = LocalDateTime.now().plusMinutes(5);
        user.setCodeExpTime(expTime);
        userRepository.save(user);
    }

    @Transactional
    public JwtResponse checkCode(String email, int code) throws WrongAuthorizationCodeException, UserNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с почтой " + email + " не найден");
        }
        if (user.getAuthCode() != code)
            throw new WrongAuthorizationCodeException("Неверный код авторизации");
        else {
            user.setAuthCode(-1);
            userRepository.save(user);
            return (jwtService.getTokens(user));
        }
    }
}
