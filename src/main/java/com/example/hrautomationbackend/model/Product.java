package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.ProductEntity;

public class Product {

    private Long id;
    private String name;
    private String pictureUrl;
    private Long productCategory;

    public static Product toModel(ProductEntity entity) {
        Product model = new Product();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setPictureUrl(entity.getPictureUrl());
        model.setProductCategory(entity.getProductCategory().getId());
        return model;
    }

    public Product() {
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

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Long getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Long productCategory) {
        this.productCategory = productCategory;
    }
}
