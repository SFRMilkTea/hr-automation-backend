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

    /** @api {get} /refresh Запрос на обновление токена
     * @apiGroup JWT
     * @apiName refresh
     * @apiHeader {String} refreshToken Рефреш токен
     * @apiSuccess {Object} token Объект, содержащий три строки: type ("Bearer"), accessToken, refreshToken
     **/

    @PostMapping
    public ResponseEntity refresh(@RequestBody String refreshToken) {
        try {
            return ResponseEntity.ok(jwtService.refresh(refreshToken));
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e);
        }
    }
}