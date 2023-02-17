package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.BuildingEntity;
import org.springframework.data.repository.CrudRepository;

public interface BuildingRepository extends CrudRepository<BuildingEntity, Long> {
    BuildingEntity findByAddress(String address);
}