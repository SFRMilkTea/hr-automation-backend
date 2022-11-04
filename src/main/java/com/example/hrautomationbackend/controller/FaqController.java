package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.*;
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

    /**
     * @api {delete} /faq/[id] Удаление вопроса по айди
     * @apiName deleteQuestion
     * @apiGroup FAQ
     * @apiParam {Number} id Уникальный идентефикатор вопроса
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если вопрос успешно удален
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@RequestHeader("Authorization") String accessToken, @PathVariable Long id) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.deleteQuestion(id));
                } catch (QuestionNotFoundException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка во время удаления вопроса");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }

    /**
     * @api {put} /faq Обновление вопроса
     * @apiName updateQuestion
     * @apiGroup FAQ
     * @apiBody {Object} question Новые данные вопроса (+ старые, если не изменялись!)
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если вопрос успешно обновлен
     **/
    @PutMapping
    public ResponseEntity updateQuestion(@RequestHeader("Authorization") String accessToken,
                                         @RequestBody QuestionEntity question) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.updateQuestion(question));
                } catch (QuestionNotFoundException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка во время апдейта вопроса");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }

}