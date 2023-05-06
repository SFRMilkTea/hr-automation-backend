package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.model.EventFormat;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface EventRepositoryCustom {
    List<EventEntity> findEventsByFilter(String name, EventFormat format, CityEntity city, Date fromDate, Date toDate, Pageable pageable);
//    PagedListHolder findEventsByFilter(String name, EventFormat format, CityEntity city, Date fromDate, Date toDate, Pageable pageable);
}
