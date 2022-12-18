package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.ProductCategoryEntity;
import com.example.hrautomationbackend.entity.ProductEntity;
import com.example.hrautomationbackend.service.ExcelService;
import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    /**
     * @apiDefine PRODUCTS
     * ПРОДУКТЫ
     */

    private final ProductService productService;
    private final ExcelService excelService;
    private final JwtService jwtService;

    public ProductController(ProductService productService, ExcelService excelService, JwtService jwtService) {
        this.productService = productService;
        this.excelService = excelService;
        this.jwtService = jwtService;
    }

    /**
     * @api {get} /products?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение списка всех продуктов
     * @apiName getProducts
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Products]} products Список всех продуктов
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
     * @api {post} /products/category/[categoryId] Добавление продукта
     * @apiName addProduct
     * @apiGroup PRODUCTS
     * @apiParam {Long} categoryId Айди категории, к которой относится добавляемый продукт
     * @apiBody {String} name Название продукта
     * @apiBody {String} code Артикул продукта
     * @apiBody {boolean} [ordered=false] Заказан ли продукт
     * @apiBody {int} quantity Количество продуктов для заказа
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Айди продукта
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductAlreadyExistException Продукт уже существует
     **/

    @PostMapping("/category/{categoryId}")
    public ResponseEntity addProduct(@RequestHeader("Authorization") String accessToken,
                                     @PathVariable(value = "categoryId") Long categoryId,
                                     @RequestBody ProductEntity product) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.addProduct(product, categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {put} /products/category/{categoryId} Обновление продукта
     * @apiName updateProduct
     * @apiGroup PRODUCTS
     * @apiParam {Long} categoryId Айди категории продукта
     * @apiBody {Long} id Айди продукта
     * @apiBody {String} name Название продукта
     * @apiBody {String} code Артикул продукта
     * @apiBody {boolean} ordered Заказан ли продукт
     * @apiBody {int} quantity Количество продуктов для заказа
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт не существует
     **/

    @PutMapping("/category/{categoryId}")
    public ResponseEntity updateProduct(@RequestHeader("Authorization") String accessToken,
                                        @PathVariable(value = "categoryId") Long categoryId,
                                        @RequestBody ProductEntity product) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.update(product, categoryId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /products/order/[id] Запросить заказ продукта
     * @apiName orderProduct
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} id Уникальный идентификатор продукта
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

    /**
     * @api {get} /products/unorder/[id] Убрать продукт из запросов на заказ
     * @apiName unorderProduct
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} id Уникальный идентификатор продукта
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт не существует
     * @apiError (Error 400) ProductNotOrderedException Продукт уже заказан
     **/

    @GetMapping("/unorder/{id}")
    public ResponseEntity unorderProduct(@RequestHeader("Authorization") String accessToken,
                                         @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.unorderProduct(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /products/category Добавление новой категории
     * @apiGroup PRODUCTS
     * @apiName addProductCategory
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {String} name Название категории
     * @apiError (Error 400) ProductCategoryAlreadyExistException Данная категория уже существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/category")
    public ResponseEntity addProductCategory(@RequestHeader("Authorization") String accessToken,
                                             @RequestBody ProductCategoryEntity category) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.addProductCategory(category);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /products/categories Получение списка категорий продуктов
     * @apiName getCategories
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Category]} categories Список всех категорий продуктов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping(path = "/categories")
    public ResponseEntity getCategories(@RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(productService.getCategories());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /products/categories/[categoryId] Вывод всех продуктов, принадлежащих категории
     * @apiName getProductsByProductCategory
     * @apiGroup PRODUCTS
     * @apiParam {Long} categoryId Айди категории продуктов
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Products]} products Список продуктов, принадлежащих заданной категории
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductCategoryNotFoundException Категория не найдена
     **/

    @GetMapping("/categories/{categoryId}")
    public ResponseEntity getProductsByProductCategory(@RequestHeader("Authorization") String accessToken,
                                                       @PathVariable(value = "categoryId") Long categoryId) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(productService.getProductsByProductCategory(categoryId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {delete} /products/categories/[id] Удаление категории продукта по айди
     * @apiName deleteProductCategory
     * @apiGroup PRODUCTS
     * @apiParam {Number} id Уникальный идентификатор категории продукта
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductCategoryNotFoundException Категория продукта с таким id не существует
     **/

    @DeleteMapping("/categories/{id}")
    public ResponseEntity deleteProductCategory(@RequestHeader("Authorization") String accessToken,
                                                @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.deleteCategory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {put} /products/category Обновление категории продукта
     * @apiName updateProductCategory
     * @apiGroup PRODUCTS
     * @apiBody {String} name Название категории
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductCategoryNotFoundException Категория продукта не существует
     **/

    @PutMapping("/category")
    public ResponseEntity updateProductCategory(@RequestHeader("Authorization") String accessToken,
                                                @RequestBody ProductCategoryEntity category) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.updateCategory(category);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /products/ordered Вывод всех продуктов, запрошенных на заказ
     * @apiName getOrderedProducts
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Products]} products Список продуктов, запрошенных на заказ
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/ordered")
    public ResponseEntity getOrderedProducts(@RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(productService.getOrderedProducts());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /products/[id] Получение продукта по айди
     * @apiName getOneProduct
     * @apiGroup PRODUCTS
     * @apiParam {Number} id Уникальный идентефикатор продукта
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id id продукта
     * @apiSuccess {String} name название продукта
     * @apiSuccess {String} code артикул
     * @apiSuccess {boolean} ordered заказан ли продукт
     * @apiSuccess {int} quantity количество продуктов на заказ
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт не существует
     **/

    @GetMapping("/{id}")
    public ResponseEntity getOneProduct(@PathVariable Long id,
                                        @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(productService.getProduct(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /products/excel Получение файла с заказанными продуктами
     * @apiName getExcel
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {byte[]} bytes если честно вообще хз что придет
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/excel")
    public ResponseEntity<byte[]> getExcel(@RequestHeader("Authorization") String accessToken) throws IOException {
        try {
            jwtService.checkAccessToken(accessToken);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_XML);
            return new ResponseEntity<>(excelService.createExcel(), headers, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @api {get} /products/search?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy]&filter=[filter] Поиск по продуктам
     * @apiName findProducts
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiParam {String} filter Строка поиска
     * @apiSuccess {List[Products]} products Список найденных продуктов (id, name, code, pictureUrl, quantity, ordered)
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/search")
    public ResponseEntity findProducts(@RequestHeader("Authorization") String accessToken,
                                       @RequestParam int pageNumber,
                                       @RequestParam int size,
                                       @RequestParam String sortBy,
                                       @RequestParam String filter) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(productService.findByString(pageable, filter));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /products/order Заказать несколько продуктов
     * @apiName orderProducts
     * @apiGroup PRODUCTS
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {List[Long]} idList id продуктов для заказа
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт не найден
     **/

    @PostMapping("/order")
    public ResponseEntity orderProducts(@RequestHeader("Authorization") String accessToken,
                                        @RequestBody List<Long> idList) {
        try {
            jwtService.checkAccessToken(accessToken);
            productService.orderProducts(idList);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
