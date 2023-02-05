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
     * @apiSuccess {Long} id id добавленного города
     * @apiError (Error 400) CityAlreadyExistException Город уже существует
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @PostMapping("/add")
    public ResponseEntity addCity(@RequestHeader("Authorization") String accessToken,
                                  @RequestBody CityEntity city) {
        try {
            jwtService.checkAccessToken(accessToken);
            cityService.addCity(city);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
