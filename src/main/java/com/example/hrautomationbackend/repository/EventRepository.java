package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.entity.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EventRepository extends PagingAndSortingRepository<EventEntity, Long> {
    EventEntity findByName(String name);
    Page<EventEntity> findAll(Pageable pageable);
    Page<EventEntity> findByCity(CityEntity city, Pageable pageable);
}