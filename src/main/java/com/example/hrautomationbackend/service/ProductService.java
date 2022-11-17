package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.ProductEntity;
import com.example.hrautomationbackend.exception.ProductAlreadyExistException;
import com.example.hrautomationbackend.exception.ProductAlreadyOrderedException;
import com.example.hrautomationbackend.exception.ProductNotFoundException;
import com.example.hrautomationbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.NoSuchElementException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public Page<ProductEntity> getProducts(Pageable pageable) {
        Page<ProductEntity> products = productRepository.findAll(pageable);
        return products;
    }

    public void delete(Long id) throws ProductNotFoundException {
        try {
            productRepository.deleteById(id);
        } catch (NoSuchElementException e) {
            throw new ProductNotFoundException("Продукт не найден");
        }
    }

    @Transactional
    public void addProduct(ProductEntity product) throws ProductAlreadyExistException {
        if (productRepository.findByCode(product.getCode()) == null) {
            productRepository.save(product);
        } else
            throw new ProductAlreadyExistException("Продукт с артикулом " + product.getCode() + " уже существует");
    }

    public void update(ProductEntity product) throws ProductNotFoundException {
        if (productRepository.findById(product.getId()).isPresent()) {
            productRepository.save(product);
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
}
