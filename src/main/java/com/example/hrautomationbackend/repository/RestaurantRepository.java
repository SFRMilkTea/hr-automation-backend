package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RestaurantRepository extends PagingAndSortingRepository<RestaurantEntity, Long> {
    RestaurantEntity findByNameAndAddress(String name, String address);
    RestaurantEntity findByName (String name);
    RestaurantEntity findByCity(Long cityId);
    Page<RestaurantEntity> findAll(Pageable pageable);
}
