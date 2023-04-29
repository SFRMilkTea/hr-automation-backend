package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.model.EventFormat;

import java.util.List;

public interface EventRepositoryCustom {
    List<EventEntity> findEventsByNameAndFormat(String name, EventFormat format);
}
