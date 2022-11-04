package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.CategoryAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionNotFoundException;
import com.example.hrautomationbackend.repository.CategoryRepository;
import com.example.hrautomationbackend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FaqService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public boolean addQuestion(QuestionEntity question) throws QuestionAlreadyExistException, CategoryAlreadyExistException {
        if (questionRepository.findByTitle(question.getTitle()) == null) {
            if (!getCategories().contains(question.getCategory()))
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

    public List<QuestionEntity> getQuestions() {
        return (List<QuestionEntity>) questionRepository.findAll();
    }

    public List<CategoryEntity> getCategories() {
        return (List<CategoryEntity>) categoryRepository.findAll();
    }

    public Boolean deleteQuestion(Long id) throws QuestionNotFoundException {
        try {
            questionRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new QuestionNotFoundException("Такой вопрос не найден");
        }
        return true;
    }

    public boolean updateQuestion(QuestionEntity question) throws QuestionNotFoundException {
        if (questionRepository.findById(question.getId()).isPresent()) {
            questionRepository.save(question);
            return true;
        } else
            throw new QuestionNotFoundException("Такой вопрос не найден");
    }
}
