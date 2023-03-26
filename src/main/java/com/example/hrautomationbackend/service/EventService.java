package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.exception.EventAlreadyExistException;
import com.example.hrautomationbackend.exception.EventNotFoundException;
import com.example.hrautomationbackend.model.Event;
import com.example.hrautomationbackend.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Long addEvent(EventEntity event) throws EventAlreadyExistException {
        if (eventRepository.findByName(event.getName()) == null) {
            eventRepository.save(event);
            return event.getId();
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
}
