package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.RoleEntity;
import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.model.User;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final RoleEntity defaultRole = new RoleEntity();

    public User getUser(Long id) throws UserNotFoundException {
        try {
            userRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new UserNotFoundException("Пользователь не найден");
        }
        return User.toModel(userRepository.findById(id).get());
    }

    public List<User> getUsers() {
        Iterable<UserEntity> users = userRepository.findAll();
        ArrayList<User> usersModel = new ArrayList<>();
        for (UserEntity user : users) {
            usersModel.add(User.toModel(user));
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


}
