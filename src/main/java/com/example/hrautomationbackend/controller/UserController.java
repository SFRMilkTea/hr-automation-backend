package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity authorization(@RequestParam String email) {
        try {
            return ResponseEntity.ok(userService.authorization(email));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @GetMapping(path = "/confirm")
    public ResponseEntity code_check(@RequestParam String email, int code) {
        try {
            return ResponseEntity.ok(userService.code_check(email, code));
        } catch (WrongAuthorizationCodeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PostMapping
    public ResponseEntity registration(@RequestBody UserEntity user) {
        try {
            userService.registration(user);
            return ResponseEntity.ok("Пользователь " + user.getUsername() + " успешно добавлен");
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

//
//    @GetMapping
//    public ResponseEntity getUser(@RequestParam Long id) {
//        try {
//            return ResponseEntity.ok(userService.getUser(id));
//        } catch (UserNotFoundException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Произошла ошибка");
//        }
//    }
}
