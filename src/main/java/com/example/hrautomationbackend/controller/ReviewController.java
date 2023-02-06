package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.entity.ReviewEntity;
import com.example.hrautomationbackend.service.JwtService;
import com.example.hrautomationbackend.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    /**
     * @apiDefine REVIEWS
     * ОТЗЫВЫ
     */

    private final ReviewService reviewService;
    private final JwtService jwtService;


    public ReviewController(ReviewService reviewService, JwtService jwtService) {
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * @api {post} /reviews/add/restaurant/[restaurantId]/user/[userId] Добавление отзыва
     * @apiName addReview
     * @apiGroup REVIEWS
     * @apiParam {Long} restaurantId Айди ресторана
     * @apiParam {Long} userId Айди пользователя
     * @apiBody {String} content Содержимое отзыва
     * @apiBody {Integer} average Средний чек
     * @apiBody {float} rating Рейтинг ресторана
     * @apiHeader {String} accessToken Аксес токен
     * @apiSuccess {Long} id Айди отзыва
     * @apiError (Error 401) AccessTokenIsNotValidException Не валидный AccessToken
     * @apiError (Error 400) UserNotFoundException Пользователь не существует
     * @apiError (Error 400) RestaurantNotFoundException Ресторан не существует
     **/

    @PostMapping("/add/restaurant/{restaurantId}/user/{userId}")
    public ResponseEntity addReview(@RequestHeader("Authorization") String accessToken,
                                    @PathVariable(value = "restaurantId") Long restaurantId,
                                    @PathVariable(value = "userId") Long userId,
                                    @RequestBody ReviewEntity review) {
        try {
            jwtService.checkAccessToken(accessToken);
            return ResponseEntity.ok(reviewService.addReview(review, restaurantId, userId));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
