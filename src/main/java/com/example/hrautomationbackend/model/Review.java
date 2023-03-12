package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.ReviewEntity;

import java.time.LocalDateTime;

public class Review {

    private Long id;
    private String content;
    private int average;
    private float rating;
    private String username;
    private String userpic;
    private LocalDateTime date;


    public static Review toModel(ReviewEntity entity) {
        Review model = new Review();
        model.setId(entity.getId());
        model.setContent(entity.getContent());
        model.setAverage(entity.getAverage());
        model.setRating(entity.getRating());
        model.setUsername(entity.getUser().getUsername());
        model.setUserpic(entity.getUser().getPictureUrl());
        model.setDate(entity.getPublicationDate());
        return model;
    }

    public Review() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpic() {
        return userpic;
    }

    public void setUserpic(String userpic) {
        this.userpic = userpic;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
