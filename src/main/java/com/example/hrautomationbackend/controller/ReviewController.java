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
     * ������
     */

    private final ReviewService reviewService;
    private final JwtService jwtService;


    public ReviewController(ReviewService reviewService, JwtService jwtService) {
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    /**
     * @api {post} /reviews/add/restaurant/[restaurantId]/user/[userId] ���������� ������
     * @apiName addReview
     * @apiGroup REVIEWS
     * @apiParam {Long} restaurantId ���� ���������
     * @apiParam {Long} userId ���� ������������
     * @apiBody {String} content ���������� ������
     * @apiBody {Integer} average ������� ���
     * @apiBody {float} rating ������� ���������
     * @apiHeader {String} accessToken ����� �����
     * @apiSuccess {Long} id ���� ������
     * @apiError (Error 401) AccessTokenIsNotValidException �� �������� AccessToken
     * @apiError (Error 400) UserNotFoundException ������������ �� ����������
     * @apiError (Error 400) RestaurantNotFoundException �������� �� ����������
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
