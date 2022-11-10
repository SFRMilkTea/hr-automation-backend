package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.ProductEntity;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    /**
     * @apiDefine PRODUCTS
     * ПРОДУКТЫ
     */

    @Autowired
    private ProductService productService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtService jwtService;

    /**
     * @api {get} /products Получение списка продуктов
     * @apiName getProducts
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Object]} products Список всех продуктов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping()
    public ResponseEntity getProducts(@RequestHeader("Authorization") String accessToken,
                                      @RequestParam int pageNumber,
                                      @RequestParam int size,
                                      @RequestParam String sortBy) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
                    return ResponseEntity.ok(productService.getProducts(pageable));
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
     * @api {delete} /products/[id] Удаление продукта по айди
     * @apiName deleteProduct
     * @apiGroup PRODUCTS
     * @apiParam {Number} id Уникальный идентификатор продукта
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если продукт успешно удален
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт с таким id не существует
     **/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteProduct(@RequestHeader("Authorization") String accessToken,
                                        @PathVariable Long id) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(productService.delete(id));
                } catch (ProductNotFoundException e) {
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

    /**
     * @api {post} /products Добавление продукта
     * @apiName addProduct
     * @apiGroup PRODUCTS
     * @apiBody {String} name Название продукта
     * @apiBody {String} code Артикул продукта
     * @apiBody {String} code Артикул продукта
     * @apiBody {Boolean} [ordered=false] Заказан ли продукт
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если продукт успешно добавлен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductAlreadyExistException Продукт уже существует
     **/

    @PostMapping
    public ResponseEntity addProduct(@RequestHeader("Authorization") String accessToken,
                                     @RequestBody ProductEntity product) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(productService.addProduct(product));
                } catch (ProductAlreadyExistException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка во время добавления");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }

    /**
     * @api {put} /products Обновление продукта
     * @apiName updateProduct
     * @apiGroup PRODUCTS
     * @apiBody {Object} product Новые данные о продукте (+ старые, если не изменялись!)
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если продукт успешно обновлен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт не существует
     **/

    @PutMapping
    public ResponseEntity updateProduct(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody ProductEntity product) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(productService.update(product));
                } catch (ProductNotFoundException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body("Произошла ошибка во время апдейта");
                }
            }
        } catch (AccessTokenIsNotValidException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Произошла ошибка");
    }
}
