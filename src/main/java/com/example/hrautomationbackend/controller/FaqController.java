package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.service.FaqService;
import com.example.hrautomationbackend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
     * @api {post} /faq/category/[categoryId] Добавление нового вопроса
     * @apiGroup FAQ
     * @apiName addQuestion
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Long} categoryId Айди категории, к которой относится добавляемый вопрос
     * @apiBody {String} title Заголовок вопроса
     * @apiBody {String} description Описание вопроса
     * @apiSuccess {boolean} result True, если вопрос успешно добавлен
     * @apiError (Error 400) QuestionAlreadyExistException Данный вопрос уже существует
     * @apiError (Error 400) CategoryNotFoundException Данная категория не существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/category/{categoryId}")
    public ResponseEntity addQuestion(@RequestHeader("Authorization") String accessToken,
                                      @PathVariable(value = "categoryId") Long categoryId,
                                      @RequestBody QuestionEntity question) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.addQuestion(question, categoryId));
                } catch (QuestionAlreadyExistException | CategoryNotFoundException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка во время добавления вопроса");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }

    /**
     * @api {get} /faq?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение списка вопросов
     * @apiName getQuestions
     * @apiGroup FAQ
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Object]} questions Список всех вопросов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping
    public ResponseEntity getQuestions(@RequestHeader("Authorization") String accessToken,
                                       @RequestParam int pageNumber,
                                       @RequestParam int size,
                                       @RequestParam String sortBy) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
                    return ResponseEntity.ok(faqService.getQuestions(pageable));
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
     * @apiError (Error 400) QuestionNotFoundException Вопрос не найден
     **/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable Long id) {
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
     * @api {put} /faq/category/[categoryId] Обновление вопроса
     * @apiName updateQuestion
     * @apiGroup FAQ
     * @apiParam {Long} categoryId Айди категории вопроса
     * @apiBody {Long} id Айди вопроса
     * @apiBody {String} title Заголовок вопроса
     * @apiBody {String} description Описание вопроса
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если вопрос успешно обновлен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) QuestionNotFoundException Вопрос не найден
     * @apiError (Error 400) CategoryNotFoundException Категория не найдена
     **/

    @PutMapping("/category/{categoryId}")
    public ResponseEntity updateQuestion(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable(value = "categoryId") Long categoryId,
                                         @RequestBody QuestionEntity question) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.updateQuestion(question, categoryId));
                } catch (QuestionNotFoundException | CategoryNotFoundException e) {
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

    /**
     * @api {get} /faq//categories/[categoryId] Вывод всех вопросов, принадлежащих категории
     * @apiName getQuestionsByCategory
     * @apiGroup FAQ
     * @apiParam {Long} categoryId Айди категории вопроса
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Questions]} questions
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) CategoryNotFoundException Категория не найдена
     **/

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity getQuestionsByCategory(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable(value = "categoryId") Long categoryId) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(faqService.getQuestionsByCategory(categoryId));
                } catch (CategoryNotFoundException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
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