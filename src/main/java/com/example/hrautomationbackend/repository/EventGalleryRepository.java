package com.example.hrautomationbackend.repository;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.entity.EventGalleryEntity;
import org.springframework.data.repository.CrudRepository;

public interface EventGalleryRepository extends CrudRepository<EventGalleryEntity, Long> {
    EventGalleryEntity findByEventId(Long eventId);
    EventGalleryEntity findByEvent(EventEntity event);
}