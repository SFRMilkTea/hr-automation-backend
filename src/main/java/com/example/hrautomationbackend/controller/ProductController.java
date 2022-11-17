package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.ProductEntity;
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
     * @api {get} /products?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение списка продуктов
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
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(productService.getProducts(pageable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            jwtService.checkAccessToken(accessToken);
            productService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /products Добавление продукта
     * @apiName addProduct
     * @apiGroup PRODUCTS
     * @apiBody {String} name Название продукта
     * @apiBody {String} code Артикул продукта
     * @apiBody {String} pictureUrl Фото продукта
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
            jwtService.checkAccessToken(accessToken);
            productService.addProduct(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            jwtService.checkAccessToken(accessToken);
            productService.update(product);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /products/order/[id] Заказать продукт
     * @apiName orderProduct
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} id Уникальный идентификатор продукта
     * @apiSuccess {Boolean} result True, если продукт успешно заказан
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт не существует
     * @apiError (Error 400) ProductAlreadyOrderedException Продукт уже заказан
     **/

    @GetMapping("/order/{id}")
    public ResponseEntity orderProduct(@RequestHeader("Authorization") String accessToken,
                                       @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.orderProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
