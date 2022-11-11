package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.QuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionRepository extends PagingAndSortingRepository<QuestionEntity, Long> {
        List<QuestionEntity> findByCategoryId(Long id);
        Page<QuestionEntity> findAll(Pageable pageable);
        QuestionEntity findByTitle (String username);
}
