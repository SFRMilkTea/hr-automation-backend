package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.RestaurantStatusEntity;
import com.example.hrautomationbackend.model.RestaurantResponse;
import com.example.hrautomationbackend.model.RestaurantUpdate;
import com.example.hrautomationbackend.service.BuildingService;
import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.RestaurantService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    /**
     * @apiDefine RESTAURANTS
     * РЕСТОРАНЫ
     */

    private final RestaurantService restaurantService;
    private final JwtService jwtService;
    private final BuildingService buildingService;

    public RestaurantController(RestaurantService restaurantService, JwtService jwtService, BuildingService buildingService) {
        this.restaurantService = restaurantService;
        this.jwtService = jwtService;
        this.buildingService = buildingService;
    }

    /**
     * @api {get} /restaurants/get/all Получение списка всех ресторанов
     * @apiName getRestaurants
     * @apiGroup RESTAURANTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Restaurants]} restaurants Список всех ресторанов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/get/all")
    public ResponseEntity getRestaurants(@RequestHeader("Authorization") String accessToken,
                                         @RequestParam int pageNumber,
                                         @RequestParam int size,
                                         @RequestParam String sortBy) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(restaurantService.getRestaurants(pageable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {delete} /restaurants/delete/[id] Удаление ресторана по айди
     * @apiName deleteRestaurant
     * @apiGroup RESTAURANTS
     * @apiParam {Number} id Уникальный идентификатор ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantNotFoundException Ресторан с таким id не существует
     **/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRestaurant(@RequestHeader("Authorization") String accessToken,
                                           @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            restaurantService.deleteRestaurant(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /add/address/status/[statusId]/city/[cityId] Добавление ресторана по адресу
     * @apiName addRestaurantByAddress
     * @apiGroup RESTAURANTS
     * @apiParam {Long} statusId Айди статуса ресторана
     * @apiParam {Long} cityId Айди города ресторана
     * @apiBody {String} name Название ресторана
     * @apiBody {String} address Адрес ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Айди ресторана
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantAlreadyExistException Ресторан уже существует
     * @apiError (Error 400) RestaurantStatusNotFoundException Указанный статус ресторана не существует
     * @apiError (Error 400) CityNotFoundException Указанный статус ресторана не существует
     **/

    @PostMapping("/add/address/status/{statusId}/city/{cityId}")
    public ResponseEntity addRestaurantByAddress(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable(value = "statusId") Long statusId,
                                                 @PathVariable(value = "cityId") Long cityId,
                                                 @RequestBody RestaurantResponse restaurant) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.addRestaurantByAddress(restaurant, statusId, cityId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /add/coordinates/status/[statusId]/city/[cityId] Добавление ресторана по координатам
     * @apiName addRestaurantByCoordinates
     * @apiGroup RESTAURANTS
     * @apiParam {Long} statusId Айди статуса ресторана
     * @apiParam {Long} cityId Айди города ресторана
     * @apiBody {String} name Название ресторана
     * @apiBody {String} address Адрес ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Айди ресторана
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantAlreadyExistException Ресторан уже существует
     * @apiError (Error 400) RestaurantStatusNotFoundException Указанный статус ресторана не существует
     * @apiError (Error 400) CityNotFoundException Указанный статус ресторана не существует
     **/

    @PostMapping("/add/coordinates/status/{statusId}/city/{cityId}")
    public ResponseEntity addRestaurantByCoordinates(@RequestHeader("Authorization") String accessToken,
                                                     @PathVariable(value = "statusId") Long statusId,
                                                     @PathVariable(value = "cityId") Long cityId,
                                                     @RequestBody RestaurantResponse restaurant) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.addRestaurantByCoordinates(restaurant, statusId, cityId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * @api {post} /restaurants/add/status Добавление нового статуса
     * @apiGroup RESTAURANTS
     * @apiName addRestaurantStatus
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {String} name Название статуса
     * @apiSuccess {Long} id id добавленного статуса
     * @apiError (Error 400) RestaurantStatusAlreadyExistException Статус уже существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/add/status")
    public ResponseEntity addRestaurantStatus(@RequestHeader("Authorization") String accessToken,
                                              @RequestBody RestaurantStatusEntity status) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.addRestaurantStatus(status));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /restaurants/get/all/statuses Получение списка статусов ресторанов
     * @apiName getStatuses
     * @apiGroup RESTAURANTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[RestaurantStatus]} statuses Список всех статусов ресторанов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping(path = "/get/all/statuses")
    public ResponseEntity getStatuses(@RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.getStatuses());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    Переделать на список фильтров

    @GetMapping("/get/status/{statusId}")
    public ResponseEntity getRestaurantsByStatus(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable(value = "statusId") Long statusId) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.getRestaurantsByStatus(statusId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /restaurants/get/city/[cityId] Получение списка ресторанов по городу
     * @apiName getRestaurantsByCity
     * @apiGroup RESTAURANTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Long} cityId Id города
     * @apiSuccess {List[Restaurants]} restaurants Список всех ресторанов города
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) CityNotFoundException Город не найден
     **/

    @GetMapping("/get/city/{cityId}")
    public ResponseEntity getRestaurantsByCity(@RequestHeader("Authorization") String accessToken,
                                               @PathVariable(value = "cityId") Long cityId) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.getRestaurantsByCity(cityId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @api {delete} /restaurants/delete/status/[id] Удаление статуса ресторана по айди
     * @apiName deleteRestaurantStatus
     * @apiGroup RESTAURANTS
     * @apiParam {Number} id Уникальный идентификатор статуса ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantStatusNotFoundException Статус ресторана с таким id не существует
     **/

    @DeleteMapping("/delete/status/{id}")
    public ResponseEntity deleteRestaurantStatus(@RequestHeader("Authorization") String accessToken,
                                                 @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            restaurantService.deleteStatus(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /restaurants/[id] Получение ресторана по айди
     * @apiName getOneRestaurant
     * @apiGroup RESTAURANTS
     * @apiParam {Long} id Уникальный идентефикатор ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Уникальный идентефикатор ресторана
     * @apiSuccess {String} name Название ресторана
     * @apiSuccess {float} rating Рейтинг ресторана
     * @apiSuccess {int} average Средний чек ресторана
     * @apiSuccess {String} address Адрес ресторана
     * @apiSuccess {double} lat Широта
     * @apiSuccess {double} lng Долгота
     * @apiSuccess {List[Review]} reviews Отзывы ресторана (id, name, rating, average, status, address)
     * @apiSuccess {RestaurantStatusEntity} status Статус ресторана
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantNotFoundException Ресторан не существует
     **/

    @GetMapping("/{id}")
    public ResponseEntity getOneRestaurant(@PathVariable Long id,
                                           @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.getRestaurant(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * @api {get} /restaurants/search?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy]&filter=[filter] Поиск по ресторанам
     * @apiName findRestaurants
     * @apiGroup RESTAURANTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiParam {String} filter Строка поиска
     * @apiSuccess {List[Restaurants]} restaurants Список найденных ресторанов (id, name, rating, average, status, address)
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/search")
    public ResponseEntity findRestaurants(@RequestHeader("Authorization") String accessToken,
                                          @RequestParam int pageNumber,
                                          @RequestParam int size,
                                          @RequestParam String sortBy,
                                          @RequestParam String filter) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(restaurantService.findByString(pageable, filter));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {delete} /restaurants/delete/building/[id] Удаление здания
     * @apiName deleteBuilding
     * @apiGroup RESTAURANTS
     * @apiParam {Number} id Уникальный идентификатор здания
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) BuildingNotFoundException Здания с таким id не существует
     **/

    @DeleteMapping("/delete/building/{id}")
    public ResponseEntity deleteBuilding(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            buildingService.deleteBuilding(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /restaurants/update/[id] Получение ресторана для редактирования
     * @apiName getRestaurantForUpdate
     * @apiGroup RESTAURANTS
     * @apiParam {Long} id Уникальный идентефикатор ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Уникальный идентефикатор ресторана
     * @apiSuccess {String} name Название ресторана
     * @apiSuccess {String} address Адрес ресторана
     * @apiSuccess {String} status Статус ресторана
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantNotFoundException Ресторан не существует
     **/

    @GetMapping("/update/{id}")
    public ResponseEntity getRestaurantForUpdate(@PathVariable Long id,
                                                 @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.getRestaurantForUpdate(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {put} /restaurants/update Обновление ресторана
     * @apiName updateRestaurant
     * @apiGroup RESTAURANTS
     * @apiBody {Long} id Уникальный идентефикатор ресторана
     * @apiBody {String} name Название ресторана
     * @apiBody {String} address Адрес ресторана
     * @apiBody {String} status Статус ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantNotFoundException Ресторан не существует
     **/

    @PutMapping("/update")
    public ResponseEntity updateRestaurant(@RequestHeader("Authorization") String accessToken,
                                           @RequestBody RestaurantUpdate restaurant) {
        try {
            jwtService.checkAccessToken(accessToken);
            restaurantService.updateRestaurant(restaurant);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
