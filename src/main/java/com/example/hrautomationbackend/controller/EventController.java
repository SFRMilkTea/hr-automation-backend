package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.model.EventFilter;
import com.example.hrautomationbackend.model.EventResponse;
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
     * @apiBody {String} [description] Описание мероприятия
     * @apiBody {String} [address] Адрес мероприятия
     * @apiBody {Date} date Дата мероприятия
     * @apiBody {Double} [lat] Широта места проведения мероприятия
     * @apiBody {Double} [lng] Долгота места проведения мероприятия
     * @apiBody {String="ONLINE","OFFLINE","COMBINED"} format Формат мероприятия
     * @apiBody {PokaHZ} [picture] заглавное фото мероприятия
     * @apiBody {List[EventMaterial]} [materials] ссылки на материалы (url, description)
     * @apiBody {Long} cityId айди города мероприятия
     * @apiError (Error 400) EventAlreadyExistException Данное мероприятие уже существует
     * @apiError (Error 400) CityNotFoundException Город с данным айди не найден
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/add")
    public ResponseEntity addEvent(@RequestHeader("Authorization") String accessToken,
                                   @RequestBody EventResponse event) {
        try {
            jwtService.checkAccessToken(accessToken);
            eventService.addEvent(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /events/get?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение событий
     * @apiName getEvents
     * @apiGroup EVENTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiBody {String} [name] Имя для фильтрации
     * @apiBody {Date} [fromDate] Нижняя граница даты для фильтрации
     * @apiBody {Date} [toDate] Верхняя граница даты для фильтрации
     * @apiBody {Long} [cityId] Id города для фильтрации
     * @apiBody {String="ONLINE","OFFLINE","COMBINED"} [format] Формат для фильтрации
     * @apiSuccess {List[Event]} events Список всех мероприятий(id, name, date, address, pictureUrl, format, cityId)
     * @apiSuccess {int} pages Общее количество страниц
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/get")
    public ResponseEntity getEvents(@RequestHeader("Authorization") String accessToken,
                                    @RequestParam int pageNumber,
                                    @RequestParam int size,
                                    @RequestParam String sortBy,
                                    @RequestBody EventFilter filter) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(eventService.getEvents(pageable, filter));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @api {get} /events/get/[id] Получение мероприятия по айди
     * @apiName getOneEvent
     * @apiGroup EVENTS
     * @apiParam {Long} id id мероприятия
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id id мероприятия
     * @apiSuccess {String} name название мероприятия
     * @apiSuccess {String} description описание мероприятия
     * @apiSuccess {Date} date дата проведения мероприятия
     * @apiSuccess {String} address место проведения мероприятия
     * @apiSuccess {String} pictureUrl заглавное фото мероприятия
     * @apiSuccess {String="ONLINE","OFFLINE","COMBINED"} format Формат мероприятия
     * @apiSuccess {List[EventMaterialEntity]} materials Список материалов мероприятия (id, url, description)
     * @apiSuccess {String} city Название город мероприятия
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


    /**
     * @api {delete} /events/delete/[id] Удаление мероприятия по айди
     * @apiName deleteEvent
     * @apiGroup EVENTS
     * @apiParam {Number} id Уникальный идентификатор мероприятия
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) EventNotFoundException Мероприятие с таким id не существует
     **/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEvent(@RequestHeader("Authorization") String accessToken,
                                      @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {put} /events/update Обновление мероприятия
     * @apiName updateEvent
     * @apiGroup EVENTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {Long} id id мероприятия
     * @apiBody {String} name название мероприятия
     * @apiBody {String} description описание мероприятия
     * @apiBody {Date} date дата проведения мероприятия
     * @apiBody {String} address место проведения мероприятия
     * @apiBody {String} pictureUrl заглавное фото мероприятия
     * @apiBody {String="ONLINE","OFFLINE","COMBINED"} format Формат мероприятия
     * @apiBody {List[String]} materials Список материалов мероприятия
     * @apiBody {Long} cityId Id города мероприятия
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductCategoryNotFoundException Категория продукта не существует
     **/


    @PutMapping("/update")
    public ResponseEntity updateEvent(@RequestHeader("Authorization") String accessToken,
                                      @RequestBody EventResponse event) {
        try {
            jwtService.checkAccessToken(accessToken);
            eventService.updateEvent(event);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
