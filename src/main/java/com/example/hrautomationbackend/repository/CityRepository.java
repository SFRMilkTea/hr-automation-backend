package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.CityEntity;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<CityEntity, Long> {
    CityEntity findByName(String name);
}