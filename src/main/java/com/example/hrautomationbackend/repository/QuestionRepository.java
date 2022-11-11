package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface QuestionRepository extends PagingAndSortingRepository<QuestionEntity, Long> {
        List<QuestionEntity> findByCategoryId(Long id);

//        @Transactional
//        void deleteByTutorialId(long tutorialId);
        Page<QuestionEntity> findAll(Pageable pageable);
        QuestionEntity findByTitle (String username);
}
