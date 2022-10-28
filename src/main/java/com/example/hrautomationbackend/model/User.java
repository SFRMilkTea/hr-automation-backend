package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.UserEntity;

public class User {
    private Long id;
    private String username;
    private String project;
    private String post;
    private String about;

    public static User toModel(UserEntity entity)
    {
        User model = new User();
        model.setId(entity.getId());
        model.setUsername(entity.getUsername());
        model.setAbout(entity.getAbout());
        model.setPost(entity.getPost());
        model.setProject(entity.getProject());
        return model;
    }

    public User() {
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

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
