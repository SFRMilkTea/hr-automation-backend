package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.model.User;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUser(Long id) throws UserNotFoundException {
        try {
            userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return User.toModel(userRepository.findById(id).get());
    }
}
