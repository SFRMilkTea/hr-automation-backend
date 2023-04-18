package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.S3Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/file")
public class FileController {

    /**
     * @apiDefine FILES
     * КАРТИНКИ
     */

    private final S3Service s3;
    private final JwtService jwtService;

    public FileController(S3Service s3, JwtService jwtService) {
        this.s3 = s3;
        this.jwtService = jwtService;
    }

    /**
     * @api {post} /file/user/[id] Загрузка картинки для пользователя
     * @apiName uploadUserPicture
     * @apiGroup FILES
     * @apiParam {Long} id Уникальный идентефикатор пользователя
     * @apiBody {MultipartFile} file Фоточка пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь с данным id не найден
     * @apiError (Error 400) MaxUploadSizeExceededException Размер картинки больше максимального
     **/

    @PostMapping("/user/{id}")
    public ResponseEntity uploadUserPicture(@RequestBody MultipartFile file,
                                            @PathVariable Long id,
                                            @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            File tempFile = s3.createSampleFile(file);
            file.transferTo(tempFile);
            s3.uploadUserPicture(tempFile, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /file/product/[id] Загрузка картинки для продукта
     * @apiName uploadProductPicture
     * @apiGroup FILES
     * @apiParam {Long} id Уникальный идентефикатор продукта
     * @apiBody {MultipartFile} file Фоточка продукта
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) ProductNotFoundException Продукт с данным id не найден
     * @apiError (Error 400) MaxUploadSizeExceededException Размер картинки больше максимального
     **/

    @PostMapping("/product/{id}")
    public ResponseEntity uploadPicture(@RequestBody MultipartFile file,
                                        @PathVariable Long id,
                                        @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            File tempFile = s3.createSampleFile(file);
            file.transferTo(tempFile);
            s3.uploadProductPicture(tempFile, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
