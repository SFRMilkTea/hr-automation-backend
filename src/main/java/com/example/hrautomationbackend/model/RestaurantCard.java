package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.RestaurantEntity;
import com.example.hrautomationbackend.entity.ReviewEntity;

import java.util.ArrayList;
import java.util.List;

public class RestaurantCard {

    private Long id;
    private String name;
    private float rating;
    private int average;
    private String status;
    private String address;
    private List<Review> reviews;
    private double lat;
    private double lng;

    public static RestaurantCard toModel(RestaurantEntity entity) {
        RestaurantCard model = new RestaurantCard();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setRating(entity.getRating());
        model.setAverage(entity.getAverage());
        model.setAddress(entity.getBuilding().getAddress());
        model.setStatus(entity.getStatus().getName());
        ArrayList<Review> reviewList = new ArrayList<>();
        for (ReviewEntity review : entity.getReviews()) {
            reviewList.add(Review.toModel(review));
        }
        model.setReviews(reviewList);
        model.setLat(entity.getBuilding().getLat());
        model.setLng(entity.getBuilding().getLng());

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

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}