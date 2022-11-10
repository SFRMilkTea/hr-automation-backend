package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.ProductEntity;
import com.example.hrautomationbackend.exception.ProductAlreadyExistException;
import com.example.hrautomationbackend.exception.ProductNotFoundException;
import com.example.hrautomationbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<ProductEntity> getProducts(Pageable pageable) {
        Page<ProductEntity> products = productRepository.findAll(pageable);
        return products;
    }

    public Boolean delete(Long id) throws ProductNotFoundException {
        try {
            productRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Продукт не найден");
        }
        return true;
    }

    public boolean addProduct(ProductEntity product) throws ProductAlreadyExistException {
        if (productRepository.findByCode(product.getCode()) == null) {
            productRepository.save(product);
            return true;
        } else
            throw new ProductAlreadyExistException("Продукт с артикулом " + product.getCode() + " уже существует");
    }

    public boolean update(ProductEntity product) throws ProductNotFoundException {
        if (productRepository.findById(product.getId()).isPresent()) {
            productRepository.save(product);
            return true;
        } else
            throw new ProductNotFoundException("Продукт с id " + product.getId() + " не существует");
    }
}
