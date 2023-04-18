package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UnableToDeleteYourselfException;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.model.User;
import com.example.hrautomationbackend.model.UserForAll;
import com.example.hrautomationbackend.model.UsersWithPages;
import com.example.hrautomationbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    public UserService(UserRepository userRepository, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
    }

    public User getUser(Long id) throws UserNotFoundException {
        UserEntity userEntity = getUserEntity(id);
        return User.toModel(userEntity);
    }

    public UserEntity getUserEntity(Long id) throws UserNotFoundException {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + " не существует"));
    }

    public UsersWithPages getUsers(Pageable pageable) {
        Page<UserEntity> users = userRepository.findAll(pageable);
        ArrayList<UserForAll> usersModel = new ArrayList<>();
        for (UserEntity user : users) {
            usersModel.add(UserForAll.toModel(user));
        }
        return UsersWithPages.toModel(usersModel, users.getTotalPages());

    }

    public void delete(Long id, String token) throws UserNotFoundException, UnableToDeleteYourselfException {
        Claims claims = jwtProvider.getAccessClaims(token);
        Long userId = Long.valueOf(claims.getSubject());
        if (userId == id) {
            throw new UnableToDeleteYourselfException("Вы не можете удалить себя");
        }
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException("Пользователь с id: " + id + " не существует");
        }
    }

    public Long registration(UserEntity user) throws UserAlreadyExistException {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            user.setAuthCode(-1);
            userRepository.save(user);
            UserEntity user1 = userRepository.findByEmail(user.getEmail());
            return user1.getId();
        } else
            throw new UserAlreadyExistException("Пользователь с email " + user.getEmail() + " уже существует");
    }

    public void update(UserEntity user) throws UserNotFoundException {
        if (userRepository.findById(user.getId()).isPresent()) {
            userRepository.save(user);
        } else
            throw new UserNotFoundException("Пользователь с id: " + user.getId() + " не существует");
    }

    public UsersWithPages findByString(Pageable pageable, String str) {
        Page<UserEntity> users = userRepository.findByUsernameContainingIgnoreCase(str, pageable);
        ArrayList<UserForAll> usersModel = new ArrayList<>();
        for (UserEntity user : users) {
            usersModel.add(UserForAll.toModel(user));
        }
        return UsersWithPages.toModel(usersModel, users.getTotalPages());

    }
}