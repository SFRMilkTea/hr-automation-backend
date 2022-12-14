package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    /**
     * @apiDefine USERS
     * СОТРУДНИКИ
     */

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * @api {get} /users/[id] Получение пользователя по айди
     * @apiName getOneUser
     * @apiGroup USERS
     * @apiParam {Number} id Уникальный идентефикатор пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id id пользователя
     * @apiSuccess {String} email email пользователя
     * @apiSuccess {String} username имя пользователя
     * @apiSuccess {String} about информация о пользователе
     * @apiSuccess {String} post должность пользователя
     * @apiSuccess {String} project проект пользователя
     * @apiSuccess {boolean} admin является ли пользователь админом
     * @apiSuccess {Date} birthDate дата рождения пользователя
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping("/{id}")
    public ResponseEntity getOneUser(@PathVariable Long id,
                                     @RequestHeader("Authorization") String accessToken) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(userService.getUser(id));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {get} /users?pageNumber=[pageNumber]&size=[size]&sortBy=[sortBy] Получение списка пользователей
     * @apiName getUsers
     * @apiGroup USERS
     * @apiHeader {String} accessToken Аксес токен
     * @apiParam {Number} pageNumber Номер страницы
     * @apiParam {Number} size Количество элементов на странице
     * @apiParam {String} sortBy Фильтр сортировки
     * @apiSuccess {List[User]} users Список всех пользователей (поля id, username, post)
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     **/

    @GetMapping()
    public ResponseEntity getUsers(@RequestHeader("Authorization") String accessToken,
                                   @RequestParam int pageNumber,
                                   @RequestParam int size,
                                   @RequestParam String sortBy) {
        try {
            jwtService.checkAccessToken(accessToken);
            Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(sortBy));
            return ResponseEntity.ok(userService.getUsers(pageable));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {delete} /users/[id] Удаление пользователя по айди
     * @apiName deleteUser
     * @apiGroup USERS
     * @apiParam {Number} id Уникальный идентефикатор пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь с таким id не существует
     **/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@RequestHeader("Authorization") String accessToken,
                                     @PathVariable Long id) {
        try {
            jwtService.checkAccessToken(accessToken);
            userService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {post} /users Добавление пользователя
     * @apiName addUser
     * @apiGroup USERS
     * @apiBody {String} email Корпоративная почта пользователя
     * @apiBody {String} username Username пользователя
     * @apiBody {String} [about] информация о пользователе
     * @apiBody {String} [post] должность пользователя
     * @apiBody {String} [project] проект пользователя
     * @apiBody {boolean} [admin=false] является ли пользователь админом
     * @apiBody {Date} [birthDate] дата рождения пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserAlreadyExistException Пользователь уже существует
     **/

    @PostMapping
    public ResponseEntity addUser(@RequestHeader("Authorization") String accessToken,
                                  @RequestBody UserEntity user) {
        try {
            jwtService.checkAccessToken(accessToken);
            userService.registration(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @api {put} /users Обновление пользователя
     * @apiName updateUser
     * @apiGroup USERS
     * @apiBody {String} email Корпоративная почта пользователя
     * @apiBody {String} username Username пользователя
     * @apiBody {String} about информация о пользователе
     * @apiBody {String} post должность пользователя
     * @apiBody {String} project проект пользователя
     * @apiBody {boolean} admin=false является ли пользователь админом
     * @apiBody {Date} birthDate дата рождения пользователя
     * @apiHeader {String} accessToken Аксес токен
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь не существует
     **/

    @PutMapping
    public ResponseEntity updateUser(@RequestHeader("Authorization") String accessToken,
                                     @RequestBody UserEntity user) {
        try {
            jwtService.checkAccessToken(accessToken);
            userService.update(user);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
