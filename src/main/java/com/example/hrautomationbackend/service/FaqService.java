package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.QuestionCategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.model.Question;
import com.example.hrautomationbackend.repository.QuestionCategoryRepository;
import com.example.hrautomationbackend.repository.QuestionRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FaqService {

    private final QuestionCategoryRepository categoryRepository;
    private final QuestionRepository questionRepository;

    public FaqService(QuestionCategoryRepository categoryRepository, QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    public void addQuestion(QuestionEntity question, Long categoryId)
            throws QuestionAlreadyExistException, QuestionCategoryNotFoundException {
        if (questionRepository.findByTitle(question.getTitle()) == null) {
            QuestionCategoryEntity category = categoryRepository
                    .findById(categoryId)
                    .orElseThrow(() -> new QuestionCategoryNotFoundException("Категория с id " + categoryId + " не существует"));
            question.setQuestionCategory(category);
            questionRepository.save(question);
        } else
            throw new QuestionAlreadyExistException("Вопрос '" + question.getTitle() + "' уже существует");
    }

    public void addQuestionCategory(QuestionCategoryEntity category) throws QuestionCategoryAlreadyExistException {
        if (categoryRepository.findByName(category.getName()) == null) {
            categoryRepository.save(category);
        } else
            throw new QuestionCategoryAlreadyExistException("Категория " + category.getName() + " уже существует");
    }

    public List<Question> getQuestions(Pageable pageable) {
        Page<QuestionEntity> questions = questionRepository.findAll(pageable);
        ArrayList<Question> questionsModel = new ArrayList<>();
        for (QuestionEntity question : questions) {
            questionsModel.add(Question.toModel(question));
        }
        return questionsModel;
    }

    public List<QuestionCategoryEntity> getCategories() {
        return (List<QuestionCategoryEntity>) categoryRepository.findAll();
    }

    public void deleteQuestion(Long id) throws QuestionNotFoundException {
        try {
            questionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new QuestionNotFoundException("Вопрос с id " + id + " не найден");
        }
    }

    public void updateQuestion(QuestionEntity question, Long categoryId) throws QuestionNotFoundException,
            QuestionCategoryNotFoundException {
        QuestionEntity questionEntity = questionRepository
                .findById(question.getId())
                .orElseThrow(() -> new QuestionNotFoundException("Вопрос с id " + question.getId() + " не существует"));
        QuestionCategoryEntity category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new QuestionCategoryNotFoundException("Категория с id " + categoryId + " не существует"));

        questionEntity.setQuestionCategory(category);
        questionRepository.save(questionEntity);
    }

    public List<QuestionEntity> getQuestionsByQuestionCategory(Long categoryId) throws QuestionCategoryNotFoundException {
        QuestionCategoryEntity category = categoryRepository
                .findById(categoryId)
                .orElseThrow(() -> new QuestionCategoryNotFoundException("Категория с id " + categoryId + " не существует"));
        return category.getQuestions();
    }

    public Question getQuestion(Long id) throws QuestionNotFoundException {
        QuestionEntity question = questionRepository
                .findById(id)
                .orElseThrow(() -> new QuestionNotFoundException("Вопрос с id " + id + " не существует"));
        return Question.toModel(question);
    }

    public void deleteCategory(Long id) throws QuestionCategoryNotFoundException {
        try {
            categoryRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new QuestionCategoryNotFoundException("Категория с id " + id + " не найдена");
        }
    }

}
