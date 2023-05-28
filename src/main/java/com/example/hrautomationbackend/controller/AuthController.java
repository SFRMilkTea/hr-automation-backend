package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.exception.TokenAlreadySavedException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.exception.WrongAuthorizationCodeException;
import com.example.hrautomationbackend.jwt.JwtResponse;
import com.example.hrautomationbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            authService.checkEmail(email);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /authorization/admin?email=[email@example.ru] Авторизация для администраторов
     * @apiName authorizationForAdmin
     * @apiGroup AUTHORIZATION
     * @apiParam {String} email Корпоративная почта пользователя
     * @apiError (Error 400) UserNotFoundException Пользователь с такой почтой не зарегистрирован
     **/

    @GetMapping("/admin")
    public ResponseEntity authorizationForAdmin(@RequestParam String email) {
        try {
            authService.checkAdminEmail(email);
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

    /**
     * @api {post} /authorization/token Подтверждение авторизации
     * @apiName sendToken
     * @apiGroup AUTHORIZATION
     * @apiBody {String} token Токен пользователя
     * @apiError (Error 400) TokenAlreadySavedException Такой токен уже сохранен
     **/

    @PostMapping(path = "/token")
    public ResponseEntity<JwtResponse> sendToken(@RequestBody String token)
            throws TokenAlreadySavedException {
        authService.saveToken(token);
        return ResponseEntity.ok().build();
    }
}
