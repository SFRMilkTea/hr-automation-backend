package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.CategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.AccessTokenIsNotValidException;
import com.example.hrautomationbackend.exception.CategoryAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionAlreadyExistException;
import com.example.hrautomationbackend.service.FaqService;
import com.example.hrautomationbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/faq")
public class FaqController {

    /**
     * @apiDefine FAQ
     * FAQ
     */

    @Autowired
    private FaqService faqService;
    @Autowired
    private JwtService jwtService;

    /**
     * @api {post} /faq/categories Добавление новой категории вопросов
     * @apiGroup FAQ
     * @apiName addCategory
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {Object} category Категория вопросов
     * @apiSuccess {boolean} result True, если категория успешно добавлена
     * @apiError (Error 400) CategoryAlreadyExistException Данная категория уже существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping()
    public ResponseEntity addQuestion(@RequestHeader("Authorization") String accessToken, @RequestBody QuestionEntity
            question) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.addQuestion(question));
                } catch (QuestionAlreadyExistException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка во время добавления вопрос");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }

    /**
     * @api {get} /faq Получение списка пользователей
     * @apiName getQuestions
     * @apiGroup FAQ
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Object]} questions Список всех вопросов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping
    public ResponseEntity getQuestions(@RequestHeader ("Authorization") String accessToken) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.getQuestions());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }

    /**
     * @api {get} /faq/categories Получение списка категорий вопросов
     * @apiName getCategories
     * @apiGroup FAQ
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Object]} categories Список всех категорий вопросов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping(path = "/categories")
    public ResponseEntity getCategories(@RequestHeader("Authorization") String accessToken) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.getCategories());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }
}