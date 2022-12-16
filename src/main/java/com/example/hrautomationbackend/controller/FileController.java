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
     * @api {post} /file/[id] Загрузка картинки
     * @apiName uploadPicture
     * @apiGroup FILES
     * @apiParam {Long} id Уникальный идентефикатор пользователя
     * @apiBody {MultipartFile} file Фоточка пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь с данным id не найден
     **/

    @PostMapping("/{id}")
    public ResponseEntity uploadPicture(@RequestBody MultipartFile file,
                                        @PathVariable Long id,
                                        @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            File tempFile = s3.createSampleFile(file);
            file.transferTo(tempFile);
            s3.uploadPicture(tempFile, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /file/[id] Получение картинки
     * @apiName downloadPicture
     * @apiGroup FILES
     * @apiParam {Long} id Уникальный идентефикатор пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {URI} uri Ссылочка на фотку
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь с данным id не найден
     **/

//    @GetMapping("/{id}")
//    public ResponseEntity downloadPicture(@PathVariable Long id,
//                                          @RequestHeader("Authorization") String accessToken) {
//        try {
//            jwtService.checkAccessToken(accessToken);
//            return ResponseEntity.ok(s3.downloadPicture(id));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

}
