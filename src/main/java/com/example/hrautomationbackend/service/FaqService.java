package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.CategoryAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionAlreadyExistException;
import com.example.hrautomationbackend.repository.CategoryRepository;
import com.example.hrautomationbackend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class FaqService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public boolean addQuestion(QuestionEntity question) throws QuestionAlreadyExistException, CategoryAlreadyExistException {
        if (questionRepository.findByTitle(question.getTitle()) == null) {
            ArrayList categories = (ArrayList) categoryRepository.findAll();
            if (!categories.contains(question.getCategory()))
                addCategory(question.getCategory());
            questionRepository.save(question);
            return true;
        } else
            throw new QuestionAlreadyExistException("Вопрос '" + question.getTitle() + "' уже существует");
    }

    public boolean addCategory(CategoryEntity category) throws CategoryAlreadyExistException {
        if (categoryRepository.findByName(category.getName()) == null) {
            categoryRepository.save(category);
            return true;
        } else
            throw new CategoryAlreadyExistException("Категория " + category.getName() + " уже существует");
    }

}
