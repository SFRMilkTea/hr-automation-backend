package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.RefreshTokenIsNotValidException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class JwtTokenService {

    private final Map<String, String> refreshStorage = new HashMap<>();
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

//    public JwtResponse getAccessToken(@NonNull String refreshToken) {
//        if (jwtProvider.validateRefreshToken(refreshToken)) {
//            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
//            final String login = claims.getSubject();
//            final String saveRefreshToken = refreshStorage.get(login);
//            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
//                final User user = userService.getByLogin(login)
//                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
//                final String accessToken = jwtProvider.generateAccessToken(user);
//                return new JwtResponse(accessToken, null);
//            }
//        }
//        return new JwtResponse(null, null);
//    }

    public JwtResponse refresh(@NonNull String refreshToken) throws UserNotFoundException, RefreshTokenIsNotValidException {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserEntity user = userRepository.findByEmail(email);
                if (user == null)
                    throw new UserNotFoundException("Пользователь с данным email не существует");
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getEmail(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new RefreshTokenIsNotValidException("Не валидный токен");
    }

}
