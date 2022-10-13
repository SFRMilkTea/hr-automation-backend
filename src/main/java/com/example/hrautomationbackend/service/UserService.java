package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
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

    public UserEntity authorization(String email) throws UserNotFoundException {
        if (userRepository.findByEmail(email) == null) {
            throw new UserNotFoundException("Пользователь с данным email не существует");
        } else {
            return ( userRepository.findByEmail(email));
            //    вот тут начинаем отправлять код на почту
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
