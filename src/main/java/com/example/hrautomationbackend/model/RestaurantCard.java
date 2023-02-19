package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.RestaurantEntity;
import com.example.hrautomationbackend.entity.ReviewEntity;

import java.util.List;

public class RestaurantCard {

    private Long id;
    private String name;
    private float rating;
    private int average;
    private String status;
    private String address;
    private List<ReviewEntity> reviews;

    public static RestaurantCard toModel(RestaurantEntity entity) {
        RestaurantCard model = new RestaurantCard();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setRating(entity.getRating());
        model.setAddress(entity.getBuilding().getAddress());
        model.setStatus(entity.getStatus().getName());
        model.setReviews(entity.getReviews());

        return model;
    }

    public RestaurantCard() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }
}