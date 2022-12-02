package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.QuestionEntity;

public class Question {

    private Long id;
    private String title;
    private String description;
    private Long categoryId;

    public static Question toModel(QuestionEntity entity) {
        Question model = new Question();
        model.setId(entity.getId());
        model.setTitle(entity.getTitle());
        model.setDescription(entity.getDescription());
        model.setCategoryId(entity.getQuestionCategory().getId());
        return model;
    }

    public Question() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
