package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.entity.EventMaterialEntity;
import com.example.hrautomationbackend.exception.CityNotFoundException;
import com.example.hrautomationbackend.exception.EventAlreadyExistException;
import com.example.hrautomationbackend.exception.EventNotFoundException;
import com.example.hrautomationbackend.model.*;
import com.example.hrautomationbackend.repository.CityRepository;
import com.example.hrautomationbackend.repository.EventMaterialRepository;
import com.example.hrautomationbackend.repository.EventRepository;
import com.example.hrautomationbackend.repository.EventRepositoryCustom;
import com.google.maps.errors.ApiException;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventRepositoryCustom eventRepositoryCustom;
    private final EventMaterialRepository eventMaterialRepository;
    private final CityRepository cityRepository;
    private final GeocoderService geocoderService;

    public EventService(EventRepository eventRepository, EventRepositoryCustom eventRepositoryCustom, EventMaterialRepository eventMaterialRepository, CityRepository cityRepository, GeocoderService geocoderService) {
        this.eventRepository = eventRepository;
        this.eventRepositoryCustom = eventRepositoryCustom;
        this.eventMaterialRepository = eventMaterialRepository;
        this.cityRepository = cityRepository;
        this.geocoderService = geocoderService;
    }

    public Long addEvent(EventResponse eventResponse) throws EventAlreadyExistException, CityNotFoundException, IOException, InterruptedException, ApiException {
        if (eventRepository.findByName(eventResponse.getName()) == null) {
            CityEntity city = cityRepository
                    .findById(eventResponse.getCityId())
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + eventResponse.getCityId() + " не найден"));
            EventResponse event = new EventResponse();
            if (eventResponse.getFormat() != EventFormat.ONLINE) {
                // создание по адресу
                if (eventResponse.getAddress() != null) {
                    event = addEventByAddress(eventResponse);
                }
                //создание по координатам
                else
                    event = addEventByCoordinates(eventResponse);

            } else {
                event = eventResponse;
            }
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
            throw new EventAlreadyExistException("Мероприятие " + eventResponse.getName() + " уже существует");
    }

    public EventsWithPages getEvents(Pageable pageable, EventFilter filter) throws CityNotFoundException {
        CityEntity city = null;
        if (filter.getCityId() != null) {
            city = cityRepository
                    .findById(filter.getCityId())
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + filter.getCityId() + " не найден"));

        }
        Date fromDate = filter.getFromDate();
        Date toDate = filter.getToDate();
        ArrayList<Event> eventsModel = new ArrayList<>();

        PagedListHolder events = eventRepositoryCustom.findEventsByFilter(filter.getName(), filter.getFormat(), city, fromDate, toDate, pageable);
        List<EventEntity> eventsList = events.getPageList();
        for (EventEntity ev : eventsList) {
            eventsModel.add(Event.toModel(ev));
        }
        eventsModel.sort((b, a) -> a.getDate().compareTo(b.getDate()));
        return EventsWithPages.toModel(eventsModel, events.getPageCount());
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

    public EventResponse addEventByCoordinates(EventResponse event) throws IOException, InterruptedException, ApiException, EventAlreadyExistException, CityNotFoundException {
        event.setAddress(geocoderService.getAddress(event.getLat(), event.getLng()));
        return event;
    }

    public EventResponse addEventByAddress(EventResponse event) throws IOException, InterruptedException, ApiException, EventAlreadyExistException, CityNotFoundException {
//        event.setLat(0);
//        event.setLng(0);


        event.setLat(Double.parseDouble(geocoderService.getLat(event.getAddress())));
        event.setLng(Double.parseDouble(geocoderService.getLng(event.getAddress())));
        return event;
    }
}
