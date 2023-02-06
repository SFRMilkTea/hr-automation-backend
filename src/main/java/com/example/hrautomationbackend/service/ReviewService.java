package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.RestaurantEntity;
import com.example.hrautomationbackend.entity.ReviewEntity;
import com.example.hrautomationbackend.entity.UserEntity;
import com.example.hrautomationbackend.exception.RestaurantNotFoundException;
import com.example.hrautomationbackend.exception.UserNotFoundException;
import com.example.hrautomationbackend.repository.RestaurantRepository;
import com.example.hrautomationbackend.repository.ReviewRepository;
import com.example.hrautomationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, RestaurantRepository restaurantRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Long addReview(ReviewEntity review, Long restaurantId, Long userId) throws RestaurantNotFoundException, UserNotFoundException {
        RestaurantEntity restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("�������� � id " + restaurantId + " �� ������"));
        review.setRestaurant(restaurant);
        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("������������ � id " + userId + " �� ������"));
        review.setUser(user);
        reviewRepository.save(review);
        return review.getId();
    }

}
