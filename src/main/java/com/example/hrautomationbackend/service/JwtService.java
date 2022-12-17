package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.AccessTokenIsNotValidException;
import com.example.hrautomationbackend.exception.RefreshTokenIsNotValidException;
import com.example.hrautomationbackend.exception.TokenIsNotValidException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.jwt.JwtResponse;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class JwtService {

    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final UserService userService;

    public JwtService(JwtProvider jwtProvider, UserService userService) {
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }

    public void checkAccessToken(String accessToken) throws AccessTokenIsNotValidException, TokenIsNotValidException {
//        if (jwtProvider.validateAccessToken(accessToken)) {
//            final Claims claims = jwtProvider.getAccessClaims(accessToken);
//            final Date expirationDate = claims.getExpiration();
//            ZoneId zoneId = ZoneId.systemDefault();
//            ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
//            Date now = Date.from(zonedDateTime.toInstant());
//            if (!(expirationDate.compareTo(now) > 0)) {
//                throw new AccessTokenIsNotValidException("Токен протух");
//            }
//            return;
//        }
//        throw new AccessTokenIsNotValidException("Не валидный токен");
    }

    public JwtResponse getTokens(UserEntity user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(String.valueOf(user.getId()), refreshToken);
        return new JwtResponse(accessToken, refreshToken, user.getId(), user.getUsername());
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws UserNotFoundException,
            RefreshTokenIsNotValidException, TokenIsNotValidException {
        Logger log = Logger.getLogger(JwtService.class.getName());
        log.info("! Refresh token " + refreshToken);
          if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String id = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(id);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                UserEntity user = userService.getUserEntity(Long.valueOf(id));
                return getTokens(user);
            }
        }
        throw new RefreshTokenIsNotValidException("Не валидный токен");
    }

}
