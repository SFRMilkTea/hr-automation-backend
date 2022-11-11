package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.QuestionEntity;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface QuestionRepository extends CrudRepository<QuestionEntity, Long> {
        List<QuestionEntity> findByCategoryId(Long id);

//        @Transactional
//        void deleteByTutorialId(long tutorialId);

        QuestionEntity findByTitle (String username);
}
