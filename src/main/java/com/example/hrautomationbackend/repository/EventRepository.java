package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.EventEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<EventEntity, Long> {
    EventEntity findByName(String name);
}