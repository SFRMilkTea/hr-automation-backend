package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Long> {
    CategoryEntity findByName (String name);
}