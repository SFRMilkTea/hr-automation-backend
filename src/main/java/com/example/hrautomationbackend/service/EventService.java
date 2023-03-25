package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.exception.EventAlreadyExistException;
import com.example.hrautomationbackend.repository.EventRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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


}
