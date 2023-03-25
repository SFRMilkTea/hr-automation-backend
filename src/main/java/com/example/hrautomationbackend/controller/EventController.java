package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.service.EventService;
import com.example.hrautomationbackend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    /**
     * @apiDefine EVENTS
     * СОЦИАЛЬНОЕ
     */

    private final EventService eventService;
    private final JwtService jwtService;

    public EventController(EventService eventService, JwtService jwtService) {
        this.eventService = eventService;
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

    @PostMapping("/add")
    public ResponseEntity addEvent(@RequestHeader("Authorization") String accessToken,
                                   @RequestBody EventEntity event) {
        try {
            jwtService.checkAccessToken(accessToken);
            eventService.addEvent(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
