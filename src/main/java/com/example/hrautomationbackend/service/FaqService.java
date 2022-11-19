package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.QuestionCategotyEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.QuestionCategotyAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionCategotyNotFoundException;
import com.example.hrautomationbackend.exception.QuestionAlreadyExistException;
import com.example.hrautomationbackend.exception.QuestionNotFoundException;
import com.example.hrautomationbackend.repository.QuestionCategotyRepository;
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

    private QuestionCategotyRepository categoryRepository;
    private QuestionRepository questionRepository;

    public FaqService(QuestionCategotyRepository categoryRepository, QuestionRepository questionRepository) {
        this.categoryRepository = categoryRepository;
        this.questionRepository = questionRepository;
    }

    public void addQuestion(QuestionEntity question, Long categoryId)
            throws QuestionAlreadyExistException, QuestionCategotyNotFoundException {
        if (questionRepository.findByTitle(question.getTitle()) == null) {
            Optional<QuestionCategotyEntity> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryRepository.findById(categoryId).isPresent()) {
                question.setQuestionCategoty(categoryOptional.get());
                questionRepository.save(question);
            } else
                throw new QuestionCategotyNotFoundException("Указанная категория не существует");
        } else
            throw new QuestionAlreadyExistException("Вопрос '" + question.getTitle() + "' уже существует");
    }

    public void addQuestionCategoty(QuestionCategotyEntity category) throws QuestionCategotyAlreadyExistException {
        if (categoryRepository.findByName(category.getName()) == null) {
            categoryRepository.save(category);
        } else
            throw new QuestionCategotyAlreadyExistException("Категория " + category.getName() + " уже существует");
    }

    public Page<QuestionEntity> getQuestions(Pageable pageable) {
        return questionRepository.findAll(pageable);
    }

    public List<QuestionCategotyEntity> getCategories() {
        return (List<QuestionCategotyEntity>) categoryRepository.findAll();
    }

    public void deleteQuestion(Long id) throws QuestionNotFoundException {
        try {
            questionRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new QuestionNotFoundException("Такой вопрос не найден", e);
        }
    }

    public void updateQuestion(QuestionEntity question, Long categoryId) throws QuestionNotFoundException,
            QuestionCategotyNotFoundException {
        if (questionRepository.findById(question.getId()).isPresent()) {
            Optional<QuestionCategotyEntity> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryOptional.isPresent()) {
                question.setQuestionCategoty(categoryOptional.get());
                questionRepository.save(question);
            } else
                throw new QuestionCategotyNotFoundException("Такая категория не найдена");
        } else
            throw new QuestionNotFoundException("Такой вопрос не найден");
    }

    public List<QuestionEntity> getQuestionsByQuestionCategoty(Long categoryId) throws QuestionCategotyNotFoundException {
        Optional<QuestionCategotyEntity> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getQuestions();
        }
        throw new QuestionCategotyNotFoundException("Такая категория не найдена");
    }
}
