package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.UserEntity;

public class UserForAll {
    private Long id;
    private String username;
    private String post;
    private String pictureUrl;

    public static UserForAll toModel(UserEntity entity) {
        UserForAll model = new UserForAll();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setPost(entity.getPost());
        model.setPictureUrl(entity.getPictureUrl());
        return model;
    }

    public UserForAll() {
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}