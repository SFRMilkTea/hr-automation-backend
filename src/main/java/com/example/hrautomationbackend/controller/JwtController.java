package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/refresh")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * @api {get} /refresh Запрос на обновление токена
     * @apiGroup JWT
     * @apiName refresh
     * @apiBody {String} refreshToken Рефреш токен
     * @apiSuccess {Object} token Объект, содержащий type ("Bearer"), accessToken, refreshToken, userId, username
     **/

    @PostMapping
    public ResponseEntity refresh(@RequestBody String refreshToken) {
        try {
            return ResponseEntity.ok(jwtService.refresh(refreshToken));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}