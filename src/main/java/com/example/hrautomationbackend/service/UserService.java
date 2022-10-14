package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CodeGenerationService codeService;

    public boolean authorization(String email) throws UserNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с данным email не существует");
        } else {
            int auth_code = codeService.generateCode();
            emailService.sendEmail(user, auth_code);
            user.setAuth_code(auth_code);
            userRepository.save(user);
            return true;
        }
    }

    public String code_check(String email, int code) throws WrongAuthorizationCodeException {
        UserEntity user = userRepository.findByEmail(email);
        if (user.getAuth_code() != code)
            throw new WrongAuthorizationCodeException("Неверный код авторизации");
        else {
            user.setAuth_code(0);
            userRepository.save(user);
            return ("код совпал");
        }
    }

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            return userRepository.save(user);
        } else
            throw new UserAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
    }
//
//    public User getUser(Long id) throws UserNotFoundException {
//        try {
//            userRepository.findById(id).get();
//        } catch (NoSuchElementException e) {
//            throw new UserNotFoundException("Пользователь не найден");
//        }
//        return User.toModel(userRepository.findById(id).get());
//    }
//
//    public Long delete(Long id) {
//        userRepository.deleteById(id);
//        return id;
//    }
}
