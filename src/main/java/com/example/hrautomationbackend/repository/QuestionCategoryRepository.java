package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.QuestionCategoryEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuestionCategoryRepository extends CrudRepository<QuestionCategoryEntity, Long> {
    QuestionCategoryEntity findByName (String name);
}