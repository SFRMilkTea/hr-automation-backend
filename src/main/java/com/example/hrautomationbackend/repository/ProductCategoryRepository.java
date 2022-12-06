package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.ProductCategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductCategoryRepository extends CrudRepository<ProductCategoryEntity, Long> {
    ProductCategoryEntity findByName (String name);
}