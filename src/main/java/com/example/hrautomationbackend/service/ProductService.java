package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.ProductCategoryEntity;
import com.example.hrautomationbackend.entity.ProductEntity;
import com.example.hrautomationbackend.entity.QuestionCategoryEntity;
import com.example.hrautomationbackend.entity.QuestionEntity;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.repository.ProductCategoryRepository;
import com.example.hrautomationbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ProductService(ProductRepository productRepository, ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public Page<ProductEntity> getProducts(Pageable pageable) {
        Page<ProductEntity> products = productRepository.findAll(pageable);
        return products;
    }

    public void delete(Long id) throws ProductNotFoundException {
        try {
            productRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Продукт с id " + id + " не найден");
        }
    }

    @Transactional
    public void addProduct(ProductEntity product, Long categoryId) throws ProductAlreadyExistException,
            ProductCategoryNotFoundException {
        if (productRepository.findByCode(product.getCode()) == null) {
            Optional<ProductCategoryEntity> categoryOptional = productCategoryRepository.findById(categoryId);
            if (productCategoryRepository.findById(categoryId).isPresent()) {
                product.setProductCategory(categoryOptional.get());
                productRepository.save(product);
            } else
                throw new ProductCategoryNotFoundException("Указанная категория не существует");
        } else
            throw new ProductAlreadyExistException("Продукт с артикулом " + product.getCode() + " уже существует");
    }

    @Transactional
    public void update(ProductEntity product, Long categoryId) throws ProductNotFoundException,
            ProductCategoryNotFoundException {
        if (productRepository.findById(product.getId()).isPresent()) {
            Optional<ProductCategoryEntity> categoryOptional = productCategoryRepository.findById(categoryId);
            if (categoryOptional.isPresent()) {
                product.setProductCategory(categoryOptional.get());
                productRepository.save(product);
            } else
                throw new ProductCategoryNotFoundException("Такая категория не найдена");
        } else
            throw new ProductNotFoundException("Продукт с id " + product.getId() + " не существует");
    }

    public void orderProduct(Long id) throws ProductNotFoundException, ProductAlreadyOrderedException {
        ProductEntity product = productRepository
                .findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт с id " + id + " не существует"));
        if (!product.isOrdered()) {
            product.setOrdered(true);
            productRepository.save(product);
        } else {
            throw new ProductAlreadyOrderedException("Продукт с id " + product.getId() + " уже заказан");
        }
    }

    public void addProductCategory(ProductCategoryEntity category) throws ProductCategoryAlreadyExistException {
        if (productCategoryRepository.findByName(category.getName()) == null) {
            productCategoryRepository.save(category);
        } else
            throw new ProductCategoryAlreadyExistException("Категория " + category.getName() + " уже существует");
    }

    public List<ProductCategoryEntity> getCategories() {
        return (List<ProductCategoryEntity>) productCategoryRepository.findAll();
    }

    public List<ProductEntity> getProductsByProductCategory(Long categoryId) throws ProductCategoryNotFoundException {
        Optional<ProductCategoryEntity> categoryOptional = productCategoryRepository.findById(categoryId);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get().getProducts();
        }
        throw new ProductCategoryNotFoundException("Категория с id " + categoryId + " не найдена");
    }

    public void deleteCategory(Long id) throws ProductCategoryNotFoundException {
        try {
            productCategoryRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ProductCategoryNotFoundException("Категория с id " + id + " не найдена");
        }
    }

    @Transactional
    public void updateCategory(ProductCategoryEntity category) throws ProductCategoryNotFoundException {
        if (productCategoryRepository.findById(category.getId()).isPresent()) {
            productCategoryRepository.save(category);
        } else
            throw new ProductCategoryNotFoundException("Категория с id " + category.getId() + " не существует");
    }

}
