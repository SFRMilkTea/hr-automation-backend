package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.service.EventService;
import com.example.hrautomationbackend.service.JwtService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
public class EventController {

    /**
     * @apiDefine EVENTS
     * СОБЫТИЯ
     */

    private final EventService eventService;
    private final JwtService jwtService;

    public EventController(EventService eventService, JwtService jwtService) {
        this.eventService = eventService;
        this.jwtService = jwtService;
    }

    /**
     * @api {post} /events/add Добавление нового мероприятия
     * @apiGroup EVENTS
     * @apiName addEvent
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {String} name Название мероприятия
     * @apiBody {String} description Описание мероприятия
     * @apiBody {Date} date Дата мероприятия
     * @apiBody {String} address Адрес мероприятия
     * @apiError (Error 400) EventAlreadyExistException Данное мероприятие уже существует
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

    /**
     * @api {get} /events/get/current?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение текущих событий
     * @apiName getCurrentEvents
     * @apiGroup EVENTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Event]} events Список текущих мероприятий(id, name, date, address, pictureUrl, online)
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/get/current")
    public ResponseEntity getCurrentEvents(@RequestHeader("Authorization") String accessToken,
                                           @RequestParam int pageNumber,
                                           @RequestParam int size,
                                           @RequestParam String sortBy) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(eventService.getCurrentEvents(pageable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /events/get/archive?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение прошедших событий
     * @apiName getArchiveEvents
     * @apiGroup EVENTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Event]} events Список прошедших мероприятий(id, name, date, address, pictureUrl, online)
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/get/archive")
    public ResponseEntity getArchiveEvents(@RequestHeader("Authorization") String accessToken,
                                           @RequestParam int pageNumber,
                                           @RequestParam int size,
                                           @RequestParam String sortBy) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(eventService.getArchiveEvents(pageable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /events/get/[id] Получение мероприятия по айди
     * @apiName getOneEvent
     * @apiGroup EVENTS
     * @apiParam {Number} id Уникальный идентефикатор мероприятия
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id id мероприятия
     * @apiSuccess {String} name название мероприятия
     * @apiSuccess {String} description описание мероприятия
     * @apiSuccess {Date} date дата проведения мероприятия
     * @apiSuccess {String} address место проведения мероприятия
     * @apiSuccess {String} pictureUrl заглавное фото мероприятия
     * @apiSuccess {boolean} online онлайн/оффлайн
     * @apiSuccess {List[EventGallery]} photos фоточки мероприятия (пока хз как это будет)
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) EventNotFoundException Мероприятие не существует
     **/

    @GetMapping("/get/{id}")
    public ResponseEntity getOneEvent(@PathVariable Long id,
                                      @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(eventService.getOneEvent(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
