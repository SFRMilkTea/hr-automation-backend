package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.RestaurantStatusEntity;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantStatusRepository extends CrudRepository<RestaurantStatusEntity, Long> {
        RestaurantStatusEntity findByName (String name);
        RestaurantStatusEntity findByIdAndName (Long id, String name);
}