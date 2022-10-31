package com.example.hrautomationbackend.controller;

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

    /**
     * @apiDefine AUTHORIZATION
     * АВТОРИЗАЦИЯ
     */

    @Autowired
    private AuthService authService;

    /** @api {get} /authorization?email=[email@example.ru] Авторизация
     * @apiName authorization
     * @apiGroup AUTHORIZATION
     * @apiParam {String} email Корпоративная почта пользователя
     * @apiSuccess {boolean} result True
     * @apiError (Error 400) UserNotFoundException Пользователь с такой почтой не зарегистрирован
     **/

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

    /** @api {get} /authorization/confirm?email=[email@example.ru]&code=[1234] Подтверждение авторизации
     * @apiName authorizationConfirm
     * @apiGroup AUTHORIZATION
     * @apiParam {String} email Корпоративная почта пользователя
     * @apiParam {Number} code Четырехзначный код, отправленный на почту
     * @apiSuccess {Object} token Объект, содержащий три строки: type ("Bearer"), accessToken, refreshToken
     **/

    @GetMapping(path = "/confirm")
    public ResponseEntity<JwtResponse> authorizationConfirm(@RequestParam String email, int code)
            throws WrongAuthorizationCodeException {
        final JwtResponse token = authService.checkCode(email, code);
        return ResponseEntity.ok(token);
    }

}
