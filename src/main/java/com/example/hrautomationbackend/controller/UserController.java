package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.AccessTokenIsNotValidException;
import com.example.hrautomationbackend.exception.UserAlreadyExistException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.jwt.JwtProvider;
import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * @apiDefine USERS
     * СОТРУДНИКИ
     */

    @Autowired
    private UserService userService;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private JwtService jwtService;

    /**
     * @api {get} /users/[id] Получение пользователя по айди
     * @apiName getOneUser
     * @apiGroup USERS
     * @apiParam {Number} id Уникальный идентефикатор пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Object} user Пользователь
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/{id}")
    public ResponseEntity getOneUser(@PathVariable Long id,
                                     @RequestHeader("Authorization") String accessToken) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(userService.getUser(id));
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
     * @api {get} /users?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение списка пользователей
     * @apiName getUsers
     * @apiGroup USERS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[Object]} users Список всех пользователей
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping()
    public ResponseEntity getUsers(@RequestHeader("Authorization") String accessToken,
                                   @RequestParam int pageNumber,
                                   @RequestParam int size,
                                   @RequestParam String sortBy) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
                    return ResponseEntity.ok(userService.getUsers(pageable));
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
     * @api {delete} /users/[id] Удаление пользователя по айди
     * @apiName deleteUser
     * @apiGroup USERS
     * @apiParam {Number} id Уникальный идентефикатор пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если пользователь успешно удален
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь с таким id не существует
     **/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@RequestHeader("Authorization") String accessToken,
                                     @PathVariable Long id) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(userService.delete(id));
                } catch (UserNotFoundException e) {
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
     * @api {post} /users Добавление пользователя
     * @apiName addUser
     * @apiGroup USERS
     * @apiBody {String} email Корпоративная почта пользователя
     * @apiBody {String} username Username пользователя
     * @apiBody {Boolean} [admin=false]  Роль пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если пользователь успешно добавлен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserAlreadyExistException Пользователь уже существует
     **/

    @PostMapping
    public ResponseEntity addUser(@RequestHeader("Authorization") String accessToken,
                                  @RequestBody UserEntity user) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(userService.registration(user));
                } catch (UserAlreadyExistException e) {
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
     * @api {put} /users Обновление пользователя
     * @apiName updateUser
     * @apiGroup USERS
     * @apiBody {Object} user Новые данные пользователя (+ старые, если не изменялись!)
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Boolean} result True, если пользователь успешно обновлен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь не существует
     **/

    @PutMapping
    public ResponseEntity updateUser(@RequestHeader("Authorization") String accessToken,
                                     @RequestBody UserEntity user) {
        try {
            if (jwtService.checkAccessToken(accessToken)) {
                try {
                    return ResponseEntity.ok(userService.update(user));
                } catch (UserNotFoundException e) {
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
