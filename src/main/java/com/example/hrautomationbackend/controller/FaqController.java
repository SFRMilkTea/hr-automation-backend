package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.QuestionCategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.service.FaqService;
import com.example.hrautomationbackend.service.JwtService;
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

    private final FaqService faqService;
    private final JwtService jwtService;

    public FaqController(FaqService faqService, JwtService jwtService) {
        this.faqService = faqService;
        this.jwtService = jwtService;
    }

    /**
     * @api {post} /faq/category/[categoryId] Добавление нового вопроса
     * @apiGroup FAQ
     * @apiName addQuestion
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Long} categoryId Айди категории, к которой относится добавляемый вопрос
     * @apiBody {String} title Заголовок вопроса
     * @apiBody {String} description Описание вопроса
     * @apiError (Error 400) QuestionAlreadyExistException Данный вопрос уже существует
     * @apiError (Error 400) QuestionCategoryNotFoundException Данная категория не существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/category/{categoryId}")
    public ResponseEntity addQuestion(@RequestHeader("Authorization") String accessToken,
                                      @PathVariable(value = "categoryId") Long categoryId,
                                      @RequestBody QuestionEntity question) {
        try {
            jwtService.checkAccessToken(accessToken);
            faqService.addQuestion(question, categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /faq/category Добавление новой категории
     * @apiGroup FAQ
     * @apiName addQuestionCategory
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {String} name Название категории
     * @apiError (Error 400) QuestionCategoryAlreadyExistException Данная категория уже существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/category")
    public ResponseEntity addQuestionCategory(@RequestHeader("Authorization") String accessToken,
                                      @RequestBody QuestionCategoryEntity category) {
        try {
            jwtService.checkAccessToken(accessToken);
            faqService.addQuestionCategory(category);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /faq?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение списка вопросов
     * @apiName getQuestions
     * @apiGroup FAQ
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Question]} questions Список всех вопросов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping
    public ResponseEntity getQuestions(@RequestHeader("Authorization") String accessToken,
                                       @RequestParam int pageNumber,
                                       @RequestParam int size,
                                       @RequestParam String sortBy) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(faqService.getQuestions(pageable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(faqService.getCategories());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {delete} /faq/[id] Удаление вопроса по айди
     * @apiName deleteQuestion
     * @apiGroup FAQ
     * @apiParam {Number} id Уникальный идентефикатор вопроса
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) QuestionNotFoundException Вопрос не найден
     **/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteQuestion(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            faqService.deleteQuestion(id);
            return ResponseEntity.ok().build();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
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
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) QuestionNotFoundException Вопрос не найден
     * @apiError (Error 400) QuestionCategoryNotFoundException Категория не найдена
     **/

    @PutMapping("/category/{categoryId}")
    public ResponseEntity updateQuestion(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable(value = "categoryId") Long categoryId,
                                         @RequestBody QuestionEntity question) {
        try {
            jwtService.checkAccessToken(accessToken);
            faqService.updateQuestion(question, categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /faq/categories/[categoryId] Вывод всех вопросов, принадлежащих категории
     * @apiName getQuestionsByQuestionCategory
     * @apiGroup FAQ
     * @apiParam {Long} categoryId Айди категории вопроса
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Questions]} questions Список вопросов, принадлежащих заданной категории
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) QuestionCategoryNotFoundException Категория не найдена
     **/

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity getQuestionsByQuestionCategory(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable(value = "categoryId") Long categoryId) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(faqService.getQuestionsByQuestionCategory(categoryId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /faq/[id] Получение вопроса по айди
     * @apiName getQuestion
     * @apiGroup FAQ
     * @apiParam {Long} id Уникальный идентификатор вопроса
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id id вопроса
     * @apiSuccess {String} title заголовок вопроса
     * @apiSuccess {String} description ответ на вопрос
     * @apiSuccess {Long} category_id id категории вопроса
     * @apiError (Error 400) QuestionNotFoundException Вопрос не найден
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/{id}")
    public ResponseEntity getQuestion(@PathVariable Long id,
                                      @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(faqService.getQuestion(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {delete} /faq/categories/[id] Удаление категории вопроса по айди
     * @apiName deleteQuestionCategory
     * @apiGroup FAQ
     * @apiParam {Number} id Уникальный идентификатор категории вопроса
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) QuestionCategoryNotFoundException Категория вопроса с таким id не существует
     **/

    @DeleteMapping("/categories/{id}")
    public ResponseEntity deleteQuestionCategory(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            faqService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
