package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.entity.EventMaterialEntity;
import com.example.hrautomationbackend.exception.CityNotFoundException;
import com.example.hrautomationbackend.exception.EventAlreadyExistException;
import com.example.hrautomationbackend.exception.EventNotFoundException;
import com.example.hrautomationbackend.model.Event;
import com.example.hrautomationbackend.model.EventFull;
import com.example.hrautomationbackend.model.EventResponse;
import com.example.hrautomationbackend.repository.CityRepository;
import com.example.hrautomationbackend.repository.EventMaterialRepository;
import com.example.hrautomationbackend.repository.EventRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
            CityEntity city = new CityEntity();
            city.setId(3L);
            city.setName("Томск");
            EventEntity event = eventRepository
                    .findById(id)
                    .orElseThrow(() -> new EventNotFoundException("Событие с id " + id + " не найдено"));
            event.setCity(city);
            eventRepository.save(event);
            eventRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new EventNotFoundException("Мероприятие с id " + id + " не найдено");
        }
    }


}
