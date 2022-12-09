package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.model.User;
import com.example.hrautomationbackend.model.UserForAll;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(Long id) throws UserNotFoundException {
        UserEntity userEntity = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не существует"));
        return User.toModel(userEntity);
    }

    public List<UserForAll> getUsers(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);
        ArrayList<UserForAll> usersModel = new ArrayList<>();
        for (UserEntity user : users) {
            usersModel.add(UserForAll.toModel(user));
        }
        return usersModel;

    }

    public void delete(Long id) throws UserNotFoundException {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с id: " + id + " не существует");
        }
    }

    public void registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            user.setAuthCode(-1);
            userRepository.save(user);
        } else
            throw new UserAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
    }

    public void update(UserEntity user) throws UserNotFoundException {
        if (userRepository.findById(user.getId()).isPresent()) {
            userRepository.save(user);
        } else
            throw new UserNotFoundException("Пользователь с id: " + user.getId() + " не существует");
    }

    public List<UserForAll> findByString(Pageable pageable, String str) {
        Page<UserEntity> users = userRepository.findAll(pageable);
        ArrayList<UserForAll> usersModel = new ArrayList<>();
        for (UserEntity user : users) {
            if (user.getUsername().contains(str)) {
                usersModel.add(UserForAll.toModel(user));
            }
        }
        return usersModel;
    }
}