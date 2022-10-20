package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping
    public ResponseEntity getOneUser(@RequestParam Long id, @RequestHeader String accessToken) {
        if (jwtProvider.validateAccessToken(accessToken)) {
            final Claims claims = jwtProvider.getAccessClaims(accessToken);
            final Date expirationDate = claims.getExpiration();

            // Получить идентификатор часового пояса
            ZoneId zoneId = ZoneId.systemDefault();
            // Конвертировать в местное время
            ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
            // Изменить тип даты
            Date now = Date.from(zonedDateTime.toInstant());

            if (expirationDate.compareTo(now) > 0) {
                try {
                    return ResponseEntity.ok(userService.getUser(id));
                } catch (UserNotFoundException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка");
                }
            }

        }
        return ResponseEntity.badRequest().body("Токен протух");
    }
}
