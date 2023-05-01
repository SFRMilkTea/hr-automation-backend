package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.model.EventFormat;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface EventRepositoryCustom {
    PagedListHolder findEventsByFilter(String name, EventFormat format, CityEntity city, Date fromDate, Date toDate, Pageable pageable);
}
