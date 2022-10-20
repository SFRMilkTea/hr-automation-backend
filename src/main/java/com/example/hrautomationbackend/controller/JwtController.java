package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refresh")
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity refresh(@RequestHeader String refreshToken) {
        try {
            return ResponseEntity.ok(jwtService.refresh(refreshToken));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }
}