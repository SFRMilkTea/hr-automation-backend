package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.CategoryAlreadyExistException;
import com.example.hrautomationbackend.exception.CategoryNotFoundException;
import com.example.hrautomationbackend.exception.QuestionAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionNotFoundException;
import com.example.hrautomationbackend.repository.CategoryRepository;
import com.example.hrautomationbackend.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FaqService {

    private CategoryRepository categoryRepository;
    private QuestionRepository questionRepository;

    public FaqService(CategoryRepository categoryRepository, QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    public void addQuestion(QuestionEntity question, Long categoryId)
            throws QuestionAlreadyExistException, CategoryNotFoundException {
        if (questionRepository.findByTitle(question.getTitle()) == null) {
            Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryRepository.findById(categoryId).isPresent()) {
                question.setCategory(categoryOptional.get());
                questionRepository.save(question);
            } else
                throw new CategoryNotFoundException("Указанная категория не существует");
        } else
            throw new QuestionAlreadyExistException("Вопрос '" + question.getTitle() + "' уже существует");
    }

    public void addCategory(CategoryEntity category) throws CategoryAlreadyExistException {
        if (categoryRepository.findByName(category.getName()) == null) {
            categoryRepository.save(category);
        } else
            throw new CategoryAlreadyExistException("Категория " + category.getName() + " уже существует");
    }

    public Page<QuestionEntity> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    public List<CategoryEntity> getCategories() {
        return (List<CategoryEntity>) categoryRepository.findAll();
    }

    public void deleteQuestion(Long id) throws QuestionNotFoundException {
        try {
            questionRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new QuestionNotFoundException("Такой вопрос не найден", e);
        }
    }

    public void updateQuestion(QuestionEntity question, Long categoryId) throws QuestionNotFoundException,
            CategoryNotFoundException {
        if (questionRepository.findById(question.getId()).isPresent()) {
            Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryOptional.isPresent()) {
                question.setCategory(categoryOptional.get());
                questionRepository.save(question);
            } else
                throw new CategoryNotFoundException("Такая категория не найдена");
        } else
            throw new QuestionNotFoundException("Такой вопрос не найден");
    }

    public List<QuestionEntity> getQuestionsByCategory(Long categoryId) throws CategoryNotFoundException {
        Optional<CategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getQuestions();
        }
        throw new CategoryNotFoundException("Такая категория не найдена");
    }
}
