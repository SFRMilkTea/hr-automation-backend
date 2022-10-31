package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.QuestionEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<QuestionEntity, Long> {
        QuestionEntity findByTitle (String username);
}
