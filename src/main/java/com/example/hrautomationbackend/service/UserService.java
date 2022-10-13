package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;

    public boolean authorization(String email) throws UserNotFoundException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("Пользователь с данным email не существует");
        } else {
            emailService.sendEmail(user);
            return true;
        }
    }

    public UserEntity registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByUsername(user.getUsername()) == null) {
            return userRepository.save(user);
        } else
            throw new UserAlreadyExistException("Пользователь " + user.getUsername() + " уже существует");
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
