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
     * @api {post} /restaurants/add Добавление ресторана
     * @apiName addRestaurant
     * @apiGroup RESTAURANTS
     * @apiBody {String} name Название ресторана
     * @apiBody {String} address Адрес ресторана
     * @apiBody {double} lat Широта
     * @apiBody {double} lng Долгота
     * @apiBody {RestaurantStatusEntity} status Статус
     * @apiBody {CityEntity} city Город
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Айди ресторана
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantAlreadyExistException Ресторан уже существует
     * @apiError (Error 400) RestaurantStatusNotFoundException Указанный статус ресторана не существует
     * @apiError (Error 400) CityNotFoundException Указанный статус ресторана не существует
     **/

    @PostMapping("/add")
    public ResponseEntity addRestaurant(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody RestaurantEntity restaurant) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(restaurantService.addRestaurant(restaurant));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
//
//    /**
//     * @api {put} /restaurants/category/{categoryId} Обновление продукта
//     * @apiName updateProduct
//     * @apiGroup PRODUCTS
//     * @apiParam {Long} categoryId Айди категории продукта
//     * @apiBody {Long} id Айди продукта
//     * @apiBody {String} name Название продукта
//     * @apiBody {String} code Артикул продукта
//     * @apiBody {boolean} ordered Заказан ли продукт
//     * @apiBody {int} quantity Количество продуктов для заказа
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     * @apiError (Error 400) ProductNotFoundException Продукт не существует
//     **/
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
//    /**
//     * @api {get} /restaurants/order/[id] Запросить заказ продукта
//     * @apiName orderProduct
//     * @apiGroup PRODUCTS
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiParam {Number} id Уникальный идентификатор продукта
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     * @apiError (Error 400) ProductNotFoundException Продукт не существует
//     * @apiError (Error 400) ProductAlreadyOrderedException Продукт уже заказан
//     **/
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
//    /**
//     * @api {get} /restaurants/unorder/[id] Убрать продукт из запросов на заказ
//     * @apiName unorderProduct
//     * @apiGroup PRODUCTS
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiParam {Number} id Уникальный идентификатор продукта
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     * @apiError (Error 400) ProductNotFoundException Продукт не существует
//     * @apiError (Error 400) ProductNotOrderedException Продукт уже заказан
//     **/
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
            restaurantService.addRestaurantStatus(status);
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

    /**
     * @api {get} /restaurants/get/status/[statusId] Вывод всех ресторанов с определенным статусом
     * @apiName getRestaurantsByStatus
     * @apiGroup RESTAURANTS
     * @apiParam {Long} statusId Айди статуса ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Restaurants]} restaurants Список ресторанов с заданным статусом
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) RestaurantStatusNotFoundException Категория не найдена
     **/

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

//    /**
//     * @api {delete} /restaurants/categories/[id] Удаление категории продукта по айди
//     * @apiName deleteProductCategory
//     * @apiGroup PRODUCTS
//     * @apiParam {Number} id Уникальный идентификатор категории продукта
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     * @apiError (Error 400) ProductCategoryNotFoundException Категория продукта с таким id не существует
//     **/
//
//    @DeleteMapping("/categories/{id}")
//    public ResponseEntity deleteProductCategory(@RequestHeader("Authorization") String accessToken,
//                                                @PathVariable Long id) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            restaurantService.deleteCategory(id);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @api {put} /restaurants/category Обновление категории продукта
//     * @apiName updateProductCategory
//     * @apiGroup PRODUCTS
//     * @apiBody {String} name Название категории
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     * @apiError (Error 400) ProductCategoryNotFoundException Категория продукта не существует
//     **/
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
//    /**
//     * @api {get} /restaurants/ordered Вывод всех продуктов, запрошенных на заказ
//     * @apiName getOrderedProducts
//     * @apiGroup PRODUCTS
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiSuccess {List[Products]} restaurants Список продуктов, запрошенных на заказ
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     **/
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
//
//    /**
//     * @api {get} /restaurants/[id] Получение продукта по айди
//     * @apiName getOneProduct
//     * @apiGroup PRODUCTS
//     * @apiParam {Number} id Уникальный идентефикатор продукта
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiSuccess {Long} id id продукта
//     * @apiSuccess {String} name название продукта
//     * @apiSuccess {String} code артикул
//     * @apiSuccess {boolean} ordered заказан ли продукт
//     * @apiSuccess {int} quantity количество продуктов на заказ
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     * @apiError (Error 400) ProductNotFoundException Продукт не существует
//     **/
//
//    @GetMapping("/{id}")
//    public ResponseEntity getOneProduct(@PathVariable Long id,
//                                        @RequestHeader("Authorization") String accessToken) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            return ResponseEntity.ok(restaurantService.getProduct(id));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * @api {get} /restaurants/excel Получение файла с заказанными продуктами
//     * @apiName getExcel
//     * @apiGroup PRODUCTS
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiSuccess {byte[]} bytes если честно вообще хз что придет
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     **/
//
//    @GetMapping("/excel")
//    public ResponseEntity<byte[]> getExcel(@RequestHeader("Authorization") String accessToken) throws IOException {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_XML);
//            return new ResponseEntity<>(excelService.createExcel(), headers, HttpStatus.OK);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
//
//    /**
//     * @api {get} /restaurants/search?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy]&filter=[filter] Поиск по продуктам
//     * @apiName findProducts
//     * @apiGroup PRODUCTS
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiParam {Number} pageNumber Номер страницы
//     * @apiParam {Number} size Количество элементов на странице
//     * @apiParam {String} sortBy Фильтр сортировки
//     * @apiParam {String} filter Строка поиска
//     * @apiSuccess {List[Products]} restaurants Список найденных продуктов (id, name, code, pictureUrl, quantity, ordered)
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     **/
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
//    /**
//     * @api {post} /restaurants/order Заказать несколько продуктов
//     * @apiName orderProducts
//     * @apiGroup PRODUCTS
//     * @apiHeader {String} accessToken Аксес токен
//     * @apiBody {List[Long]} idList id продуктов для заказа
//     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
//     * @apiError (Error 400) ProductNotFoundException Продукт не найден
//     **/
//
//    @PostMapping("/order")
//    public ResponseEntity orderProducts(@RequestHeader("Authorization") String accessToken,
//                                        @RequestBody List<Long> idList) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            restaurantService.orderProducts(idList);
//            return ResponseEntity.ok().build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
}
