package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.CategoryEntity;
import com.example.hrautomationbackend.exception.AccessTokenIsNotValidException;
import com.example.hrautomationbackend.exception.CategoryAlreadyExistException;
import com.example.hrautomationbackend.service.FaqService;
import com.example.hrautomationbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faq")
public class FaqController {

    @Autowired
    private FaqService faqService;
    @Autowired
    private JwtService jwtService;

    /**
     * @api {get} /refresh Запрос на обновление токена
     * @apiGroup JWT
     * @apiName refresh
     * @apiHeader {String} refreshToken Рефреш токен
     * @apiSuccess {Object} token Объект, содержащий три строки: type ("Bearer"), accessToken, refreshToken
     **/

    @PostMapping
    public ResponseEntity addCategory(@RequestHeader("Authorization") String accessToken, @RequestBody CategoryEntity category) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.addCategory(category));
                } catch (CategoryAlreadyExistException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка во время добавления категории");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }
}