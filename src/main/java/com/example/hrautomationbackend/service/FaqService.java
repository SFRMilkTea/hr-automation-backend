package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CategoryEntity;
import com.example.hrautomationbackend.exception.CategoryAlreadyExistException;
import com.example.hrautomationbackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FaqService {

    @Autowired
    private CategoryRepository categoryRepository;

    public boolean addCategory(CategoryEntity category) throws CategoryAlreadyExistException {
        if (categoryRepository.findByName(category.getName()) == null) {
            categoryRepository.save(category);
            return true;
        } else
            throw new CategoryAlreadyExistException("Категория " + category.getName() + " уже существует");
    }

}
