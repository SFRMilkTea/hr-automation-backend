package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.QuestionCategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.QuestionAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionCategoryAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionCategoryNotFoundException;
import com.example.hrautomationbackend.exception.QuestionNotFoundException;
import com.example.hrautomationbackend.repository.QuestionCategoryRepository;
import com.example.hrautomationbackend.repository.QuestionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class FaqService {

    private QuestionCategoryRepository categoryRepository;
    private QuestionRepository questionRepository;

    public FaqService(QuestionCategoryRepository categoryRepository, QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    public void addQuestion(QuestionEntity question, Long categoryId)
            throws QuestionAlreadyExistException, QuestionCategoryNotFoundException {
        if (questionRepository.findByTitle(question.getTitle()) == null) {
            Optional<QuestionCategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryRepository.findById(categoryId).isPresent()) {
                question.setQuestionCategory(categoryOptional.get());
                questionRepository.save(question);
            } else
                throw new QuestionCategoryNotFoundException("Указанная категория не существует");
        } else
            throw new QuestionAlreadyExistException("Вопрос '" + question.getTitle() + "' уже существует");
    }

    public void addQuestionCategory(QuestionCategoryEntity category) throws QuestionCategoryAlreadyExistException {
        if (categoryRepository.findByName(category.getName()) == null) {
            categoryRepository.save(category);
        } else
            throw new QuestionCategoryAlreadyExistException("Категория " + category.getName() + " уже существует");
    }

    public Page<QuestionEntity> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    public List<QuestionCategoryEntity> getCategories() {
        return (List<QuestionCategoryEntity>) categoryRepository.findAll();
    }

    public void deleteQuestion(Long id) throws QuestionNotFoundException {
        try {
            questionRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new QuestionNotFoundException("Такой вопрос не найден", e);
        }
    }

    public void updateQuestion(QuestionEntity question, Long categoryId) throws QuestionNotFoundException,
            QuestionCategoryNotFoundException {
        if (questionRepository.findById(question.getId()).isPresent()) {
            Optional<QuestionCategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryOptional.isPresent()) {
                question.setQuestionCategory(categoryOptional.get());
                questionRepository.save(question);
            } else
                throw new QuestionCategoryNotFoundException("Такая категория не найдена");
        } else
            throw new QuestionNotFoundException("Такой вопрос не найден");
    }

    public List<QuestionEntity> getQuestionsByQuestionCategory(Long categoryId) throws QuestionCategoryNotFoundException {
        Optional<QuestionCategoryEntity> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getQuestions();
        }
        throw new QuestionCategoryNotFoundException("Такая категория не найдена");
    }

    public QuestionEntity getQuestion(Long id) throws QuestionNotFoundException {

        QuestionEntity question = questionRepository
                .findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Вопрос с id " + id + " не существует"));
//        return question.getQuestionCategory();
        return question;
    }

    public QuestionCategoryEntity getQuestionCategory(Long id) throws QuestionNotFoundException {
        QuestionEntity question = questionRepository
                .findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Вопрос с id " + id + " не существует"));
        return question.getQuestionCategory();
    }

}
