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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    private final Map<String, String> refreshStorage = new HashMap<>();
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;

    public boolean checkAccessToken(String accessToken) throws AccessTokenIsNotValidException {
        if (jwtProvider.validateAccessToken(accessToken)) {
            final Claims claims = jwtProvider.getAccessClaims(accessToken);
            final Date expirationDate = claims.getExpiration();
            ZoneId zoneId = ZoneId.systemDefault();
            ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
            Date now = Date.from(zonedDateTime.toInstant());
            return expirationDate.compareTo(now) > 0;
        }
        throw new AccessTokenIsNotValidException("Не валидный токен");
    }

    public JwtResponse getTokens(UserEntity user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), refreshToken);
        return new JwtResponse(accessToken, refreshToken);
    }


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
                return getTokens(user);
            }
        }
        throw new RefreshTokenIsNotValidException("Не валидный токен");
    }

}
