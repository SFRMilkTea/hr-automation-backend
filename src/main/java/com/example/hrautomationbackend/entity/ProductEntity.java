package com.example.hrautomationbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private String pictureUrl;
    private boolean isOrdered;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_category_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private ProductCategoryEntity productCategory;


    public ProductEntity() {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }

    public ProductCategoryEntity getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryEntity productCategory) {
        this.productCategory = productCategory;
    }
}
