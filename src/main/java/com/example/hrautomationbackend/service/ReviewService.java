package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.RestaurantEntity;
import com.example.hrautomationbackend.entity.ReviewEntity;
import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.RestaurantNotFoundException;
import com.example.hrautomationbackend.exception.ReviewNotFoundException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.model.Review;
import com.example.hrautomationbackend.repository.RestaurantRepository;
import com.example.hrautomationbackend.repository.ReviewRepository;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantService restaurantService;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, RestaurantRepository restaurantRepository, RestaurantService restaurantService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.restaurantService = restaurantService;
    }

    @Transactional
    public Long addReview(ReviewEntity review, Long restaurantId, Long userId) throws RestaurantNotFoundException, UserNotFoundException {
        RestaurantEntity restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + restaurantId + " не найден"));
        review.setRestaurant(restaurant);
        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден"));
        review.setUser(user);
        review.setPublicationDate(LocalDateTime.now());
        reviewRepository.save(review);
        restaurantService.calculateRating(restaurant);
        restaurantService.calculateAverage(restaurant);
        return review.getId();
    }

    public List<Review> getReviewsByRestaurant(Long restaurantId) throws RestaurantNotFoundException {
        RestaurantEntity restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + restaurantId + " не найден"));

        ArrayList<Review> reviewList = new ArrayList<>();
        for (ReviewEntity review : restaurant.getReviews()) {
            reviewList.add(Review.toModel(review));
        }
        reviewList.sort(Comparator.comparing(Review::getDate).reversed());
        return reviewList;
    }

    public void deleteReview(Long id) throws ReviewNotFoundException {
        try {
            RestaurantEntity restaurant = reviewRepository.findById(id).get().getRestaurant();
            reviewRepository.deleteById(id);
            restaurantService.calculateAverage(restaurant);
            restaurantService.calculateRating(restaurant);

        } catch (EmptyResultDataAccessException e) {
            throw new ReviewNotFoundException("Отзыв с id " + id + " не найден");
        }
    }

}
