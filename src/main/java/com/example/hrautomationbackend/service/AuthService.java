package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.RoleEntity;
import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CodeGenerationService codeService;
    @Autowired
    private JwtProvider jwtProvider;

    private final Map<String, String> refreshStorage = new HashMap<>();
    private final RoleEntity defaultRole = new RoleEntity();

    public boolean sendCode(String email) throws UserNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с данным email не существует");
        } else {
            int authCode = codeService.generateCode();
            emailService.sendEmail(user, authCode);
            user.setAuthCode(authCode);
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

    public void registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            if(user.getRole() == null) {
                defaultRole.setId(2L);
                user.setRole(defaultRole);
            }
            userRepository.save(user);
        } else
            throw new UserAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
    }

//    public Long delete(Long id) {
//        userRepository.deleteById(id);
//        return id;
//    }
}
