package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.*;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.model.*;
import com.example.hrautomationbackend.repository.CityRepository;
import com.example.hrautomationbackend.repository.EventMaterialRepository;
import com.example.hrautomationbackend.repository.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMaterialRepository eventMaterialRepository;
    private final CityRepository cityRepository;

    public EventService(EventRepository eventRepository, EventMaterialRepository eventMaterialRepository, CityRepository cityRepository) {
        this.eventRepository = eventRepository;
        this.eventMaterialRepository = eventMaterialRepository;
        this.cityRepository = cityRepository;
    }

    public Long addEvent(EventResponse event) throws EventAlreadyExistException, CityNotFoundException {
        if (eventRepository.findByName(event.getName()) == null) {
            CityEntity city = cityRepository
                    .findById(event.getCityId())
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + event.getCityId() + " не найдено"));
            EventEntity entity = EventEntity.toEntity(event, city);
            eventRepository.save(entity);
            if (event.getMaterials() != null) {
                for (EventMaterialEntity material : event.getMaterials()) {
                    EventMaterialEntity eventMaterial = new EventMaterialEntity();
                    eventMaterial.setUrl(material.getUrl());
                    eventMaterial.setDescription(material.getDescription());
                    eventMaterial.setEvent(entity);
                    eventMaterialRepository.save(eventMaterial);
                }
            }
            eventRepository.save(entity);
            return entity.getId();
        } else
            throw new EventAlreadyExistException("Мероприятие " + event.getName() + " уже существует");
    }

    public List<Event> getEvents(Pageable pageable) {
        Page<EventEntity> events = eventRepository.findAll(pageable);
        ArrayList<Event> eventsModel = new ArrayList<>();
        for (EventEntity event : events) {
            eventsModel.add(Event.toModel(event));
        }
        eventsModel.sort((b, a) -> a.getDate().compareTo(b.getDate()));
        return eventsModel;
    }

    public EventFull getOneEvent(Long id) throws EventNotFoundException {
        EventEntity event = eventRepository
                .findById(id)
                .orElseThrow(() -> new EventNotFoundException("Событие с id " + id + " не найдено"));
        return EventFull.toModel(event);
    }

    public void deleteEvent(Long id) throws EventNotFoundException {
        try {
            eventRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EventNotFoundException("Мероприятие с id " + id + " не найдено");
        }
    }

    @Transactional
    public void updateEvent(EventResponse event) throws EventNotFoundException, CityNotFoundException {
        if (eventRepository.findById(event.getId()).isPresent()) {
            CityEntity city = cityRepository
                    .findById(event.getCityId())
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + event.getCityId() + " не найден"));
            eventRepository.save(EventEntity.toEntity(event, city));
        } else
            throw new EventNotFoundException("Мероприятие с id " + event.getId() + " не существует");
    }

    public EventsWithPages getEventsByCity(Long cityId, Pageable pageable) throws CityNotFoundException {
        CityEntity city = cityRepository
                .findById(cityId)
                .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));

        Page<EventEntity> events = eventRepository.findByCity(city, pageable);
        ArrayList<Event> eventsModel = new ArrayList<>();
        for (EventEntity event : events) {
            eventsModel.add(Event.toModel(event));
        }

        return EventsWithPages.toModel(eventsModel, events.getTotalPages());
    }

}
