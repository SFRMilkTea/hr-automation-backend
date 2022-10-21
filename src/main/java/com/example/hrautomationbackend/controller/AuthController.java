package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authorization")
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping
    public ResponseEntity authorization(@RequestParam String email) {
        try {
            return ResponseEntity.ok(authService.sendCode(email));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity<JwtResponse> authorizationConfirm(@RequestParam String email, int code)
            throws WrongAuthorizationCodeException {
        final JwtResponse token = authService.checkCode(email, code);
        return ResponseEntity.ok(token);
    }

    @PostMapping
    public ResponseEntity registration(@RequestBody UserEntity user) {
        try {
            authService.registration(user);
            return ResponseEntity.ok("Пользователь " + user.getUsername() + " успешно добавлен");
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }
}
