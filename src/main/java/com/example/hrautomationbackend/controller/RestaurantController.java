package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.RestaurantEntity;
import com.example.hrautomationbackend.entity.RestaurantStatusEntity;
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

    public RestaurantController(RestaurantService restaurantService, JwtService jwtService) {
        this.restaurantService = restaurantService;
        this.jwtService = jwtService;
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
     * @api {post} /add/status/[statusId]/city/[cityId] Добавление ресторана
     * @apiName addRestaurant
     * @apiGroup RESTAURANTS
     * @apiParam {Long} statusId Айди статуса ресторана
     * @apiParam {Long} cityId Айди города ресторана
     * @apiBody {String} name Название ресторана
     * @apiBody {String} address Адрес ресторана
     * @apiBody {double} lat Широта
     * @apiBody {double} lng Долгота
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Айди ресторана
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantAlreadyExistException Ресторан уже существует
     * @apiError (Error 400) RestaurantStatusNotFoundException Указанный статус ресторана не существует
     * @apiError (Error 400) CityNotFoundException Указанный статус ресторана не существует
     **/

    @PostMapping("/add/status/{statusId}/city/{cityId}")
    public ResponseEntity addRestaurant(@RequestHeader("Authorization") String accessToken,
                                        @PathVariable(value = "statusId") Long statusId,
                                        @PathVariable(value = "cityId") Long cityId,
                                        @RequestBody RestaurantEntity restaurant) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.addRestaurant(restaurant, statusId, cityId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//

//
//    @PutMapping("/category/{categoryId}")
//    public ResponseEntity updateProduct(@RequestHeader("Authorization") String accessToken,
//                                        @PathVariable(value = "categoryId") Long categoryId,
//                                        @RequestBody ProductEntity restaurant) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            restaurantService.update(restaurant, categoryId);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//

//
//    @GetMapping("/order/{id}")
//    public ResponseEntity orderProduct(@RequestHeader("Authorization") String accessToken,
//                                       @PathVariable Long id) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            restaurantService.orderProduct(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//

//    @GetMapping("/unorder/{id}")
//    public ResponseEntity unorderProduct(@RequestHeader("Authorization") String accessToken,
//                                         @PathVariable Long id) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            restaurantService.unorderProduct(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//


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


//
//    @PutMapping("/category")
//    public ResponseEntity updateProductCategory(@RequestHeader("Authorization") String accessToken,
//                                                @RequestBody ProductCategoryEntity category) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            restaurantService.updateCategory(category);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//

//
//    @GetMapping("/ordered")
//    public ResponseEntity getOrderedProducts(@RequestHeader("Authorization") String accessToken) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            return ResponseEntity.ok(restaurantService.getOrderedProducts());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * @api {get} /restaurants/get/[id] Получение ресторана по айди
     * @apiName getOneRestaurant
     * @apiGroup RESTAURANTS
     * @apiParam {Long} id Уникальный идентефикатор ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Уникальный идентефикатор ресторана
     * @apiSuccess {String} name Название ресторана
     * @apiSuccess {float} rating Рейтинг ресторана
     * @apiSuccess {int} average Средний чек ресторана
     * @apiSuccess {String} address Адрес ресторана
     * @apiSuccess {double} lat Широта расположения ресторана
     * @apiSuccess {double} lng Долгота расположения ресторана
     * @apiSuccess {List[ReviewEntity]} reviews Отзывы ресторана
     * @apiSuccess {RestaurantStatusEntity} status Статус ресторана
     * @apiSuccess {CityEntity} city Город ресторана
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


//
//    @GetMapping("/search")
//    public ResponseEntity findProducts(@RequestHeader("Authorization") String accessToken,
//                                       @RequestParam int pageNumber,
//                                       @RequestParam int size,
//                                       @RequestParam String sortBy,
//                                       @RequestParam String filter) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
//            return ResponseEntity.ok(restaurantService.findByString(pageable, filter));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
}
