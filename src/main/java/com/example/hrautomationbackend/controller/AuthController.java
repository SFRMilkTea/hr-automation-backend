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

    /** @api {get} /authorization?email=[email@example.ru] Авторизация
     * @apiGroup АВТОРИЗАЦИЯ
     * @apiName authorization
     * @apiParam {String} email Корпоративная почта пользователя
     *
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
     * @apiGroup АВТОРИЗАЦИЯ
     * @apiName authorizationConfirm
     * @apiParam {String} email Корпоративная почта пользователя
     * @apiParam {Number} code Четырехзначный код, отправленный на почту
     *
     **/

    @GetMapping(path = "/confirm")
    public ResponseEntity<JwtResponse> authorizationConfirm(@RequestParam String email, int code)
            throws WrongAuthorizationCodeException {
        final JwtResponse token = authService.checkCode(email, code);
        return ResponseEntity.ok(token);
    }

}
