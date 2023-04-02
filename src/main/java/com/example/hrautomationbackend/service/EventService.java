package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.entity.EventMaterialEntity;
import com.example.hrautomationbackend.exception.EventAlreadyExistException;
import com.example.hrautomationbackend.exception.EventNotFoundException;
import com.example.hrautomationbackend.model.Event;
import com.example.hrautomationbackend.model.EventResponse;
import com.example.hrautomationbackend.repository.EventMaterialRepository;
import com.example.hrautomationbackend.repository.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMaterialRepository eventMaterialRepository;

    public EventService(EventRepository eventRepository, EventMaterialRepository eventMaterialRepository) {
        this.eventRepository = eventRepository;
        this.eventMaterialRepository = eventMaterialRepository;
    }

    public Long addEvent(EventResponse event) throws EventAlreadyExistException {
        if (eventRepository.findByName(event.getName()) == null) {

            EventEntity entity = EventEntity.toEntity(event);
            eventRepository.save(entity);
            if (event.getMaterials()!=null) {
                for (String material : event.getMaterials()) {
                    EventMaterialEntity eventMaterial = new EventMaterialEntity();
                    eventMaterial.setUrl(material);
                    eventMaterial.setEvent(entity);
                    eventMaterialRepository.save(eventMaterial);
                }
            }
            eventRepository.save(entity);
            return entity.getId();
        } else
            throw new EventAlreadyExistException("Мероприятие " + event.getName() + " уже существует");
    }

    public List<Event> getCurrentEvents(Pageable pageable) {
        Page<EventEntity> events = eventRepository.findAll(pageable);
        ArrayList<Event> eventsModel = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
        Date now = Date.from(zonedDateTime.toInstant());
        for (EventEntity event : events) {
            if (event.getDate().compareTo(now) >= 0) {
                eventsModel.add(Event.toModel(event));
            }
        }
        eventsModel.sort((b, a) -> a.getDate().compareTo(b.getDate()));
        return eventsModel;
    }

    public List<Event> getArchiveEvents(Pageable pageable) {
        Page<EventEntity> events = eventRepository.findAll(pageable);
        ArrayList<Event> eventsModel = new ArrayList<>();
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = LocalDateTime.now().atZone(zoneId);
        Date now = Date.from(zonedDateTime.toInstant());
        for (EventEntity event : events) {
            if (event.getDate().compareTo(now) < 0) {
                eventsModel.add(Event.toModel(event));
            }
        }
        eventsModel.sort((b, a) -> a.getDate().compareTo(b.getDate()));
        return eventsModel;
    }

    public EventEntity getOneEvent(Long id) throws EventNotFoundException {
        return eventRepository
                .findById(id)
                .orElseThrow(() -> new EventNotFoundException("Событие с id " + id + " не найдено"));
    }

    public void deleteEvent(Long id) throws EventNotFoundException {
        try {
            eventRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EventNotFoundException("Мероприятие с id " + id + " не найдено");
        }
    }


}
