package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.model.User;
import com.example.hrautomationbackend.model.UserForAll;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public List<User> getUsers(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);
        ArrayList<User> usersModel = new ArrayList<>();
        for (UserEntity user : users) {
            usersModel.add(UserForAll.toModel(user));
        }
        return usersModel;

    }

    public Boolean delete(Long id) throws UserNotFoundException {
        try {
            userRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return true;
    }

    public boolean registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            user.setAuthCode(-1);
            userRepository.save(user);
            return true;
        } else
            throw new UserAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
    }

    public boolean update(UserEntity user) throws UserNotFoundException {
        if (userRepository.findById(user.getId()).isPresent()) {
            userRepository.save(user);
            return true;
        } else
            throw new UserNotFoundException("Пользователь не существует");
    }

}
