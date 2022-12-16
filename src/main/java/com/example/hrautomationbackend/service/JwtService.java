package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.AccessTokenIsNotValidException;
import com.example.hrautomationbackend.exception.RefreshTokenIsNotValidException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public JwtService(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
    }

    public void checkAccessToken(String accessToken) throws AccessTokenIsNotValidException {
//        if (jwtProvider.validateAccessToken(accessToken)) {
//            final Claims claims = jwtProvider.getAccessClaims(accessToken);
//            final Date expirationDate = claims.getExpiration();
//            ZoneId zoneId = ZoneId.systemDefault();
//            ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
//            Date now = Date.from(zonedDateTime.toInstant());
//            if (!(expirationDate.compareTo(now) > 0)) {
//                throw new AccessTokenIsNotValidException("Токен протух");
//            }
//        }
//        throw new AccessTokenIsNotValidException("Не валидный токен");
    }

    public JwtResponse getTokens(UserEntity user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), refreshToken);
        return new JwtResponse(accessToken, refreshToken, user.getId(), user.getUsername());
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws UserNotFoundException, RefreshTokenIsNotValidException {
          if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String email = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(email);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserEntity user = userRepository.findByEmail(email);
                if (user == null)
                    throw new UserNotFoundException("Пользователь с email " + email + " не существует");
                return getTokens(user);
            }
        }
        throw new RefreshTokenIsNotValidException("Не валидный токен");
    }

}
