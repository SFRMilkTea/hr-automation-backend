package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.exception.AccessTokenIsNotValidException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtService jwtService;


    @GetMapping("/{id}")
    public ResponseEntity getOneUser(@PathVariable Long id, @RequestHeader String accessToken) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(userService.getUser(id));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }


    @GetMapping
    public ResponseEntity getUsers(@RequestHeader String accessToken) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(userService.getUsers());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.delete(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

}
