package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authorization")
public class AuthController {

    /**
     * @apiDefine AUTHORIZATION
     * АВТОРИЗАЦИЯ
     */

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * @api {get} /authorization?email=[email@example.ru] Авторизация
     * @apiName authorization
     * @apiGroup AUTHORIZATION
     * @apiParam {String} email Корпоративная почта пользователя
     * @apiError (Error 400) UserNotFoundException Пользователь с такой почтой не зарегистрирован
     **/

    @GetMapping
    public ResponseEntity authorization(@RequestParam String email) {
        try {
            authService.sendCode(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /authorization/confirm?email=[email@example.ru]&code=[1234] Подтверждение авторизации
     * @apiName authorizationConfirm
     * @apiGroup AUTHORIZATION
     * @apiParam {String} email Корпоративная почта пользователя
     * @apiParam {Number} code Четырехзначный код, отправленный на почту
     * @apiSuccess {Object} token Объект, содержащий type ("Bearer"), accessToken, refreshToken, userId, username
     * @apiError (Error 400) UserNotFoundException Пользователь с такой почтой не зарегистрирован
     * @apiError (Error 400) WrongAuthorizationCodeException Неверный код авторизации
     **/

    @GetMapping(path = "/confirm")
    public ResponseEntity<JwtResponse> authorizationConfirm(@RequestParam String email, int code)
            throws WrongAuthorizationCodeException, UserNotFoundException {
        final JwtResponse token = authService.checkCode(email, code);
        return ResponseEntity.ok(token);
    }
}
