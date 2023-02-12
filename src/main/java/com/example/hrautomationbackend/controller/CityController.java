package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.service.CityService;
import com.example.hrautomationbackend.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cities")
public class CityController {

    /**
     * @apiDefine CITIES
     * ГОРОДА
     */

    private final CityService cityService;
    private final JwtService jwtService;

    public CityController(CityService cityService, JwtService jwtService) {
        this.cityService = cityService;
        this.jwtService = jwtService;
    }

    /**
     * @api {post} /cities/add Добавление нового города
     * @apiGroup CITIES
     * @apiName addCity
     * @apiHeader {String} accessToken Аксес токен
     * @apiBody {String} name Название города
     * @apiBody {double} lat Широта города
     * @apiBody {double} lng Долгота города
     * @apiSuccess {Long} id id добавленного города
     * @apiError (Error 400) CityAlreadyExistException Город уже существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/add")
    public ResponseEntity addCity(@RequestHeader("Authorization") String accessToken,
                                  @RequestBody CityEntity city) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(cityService.addCity(city));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /cities/get/all Получение списка городов
     * @apiName getCities
     * @apiGroup CITIES
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {List[Cities]} cities Список всех городов
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping(path = "/get/all")
    public ResponseEntity getCities(@RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(cityService.getCities());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /**
      * @api {delete} /cities/delete/[id] Удаление города по айди
     * @apiName deleteCity
     * @apiGroup CITIES
     * @apiParam {Number} id Уникальный идентификатор города
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) CityNotFoundException Город с таким id не существует
     **/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCity(@RequestHeader("Authorization") String accessToken,
                                     @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            cityService.deleteCity(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
