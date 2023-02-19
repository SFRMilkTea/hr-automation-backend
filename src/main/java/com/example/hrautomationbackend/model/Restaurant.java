package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.RestaurantEntity;

public class Restaurant {

    private Long id;
    private String name;
    private float rating;
    private int average;
    private String status;

    public static Restaurant toModel(RestaurantEntity entity) {
        Restaurant model = new Restaurant();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setRating(entity.getRating());

        model.setStatus(entity.getStatus().getName());

        return model;
    }

    public Restaurant() {
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
}