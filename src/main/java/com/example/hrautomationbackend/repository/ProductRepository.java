package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {

    ProductEntity findByCode(String code);
    Page<ProductEntity> findAll(Pageable pageable);
}