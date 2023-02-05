package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.exception.CityAlreadyExistException;
import com.example.hrautomationbackend.repository.CityRepository;
import org.springframework.stereotype.Service;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public Long addCity(CityEntity city) throws CityAlreadyExistException {
        if (cityRepository.findByName(city.getName()) == null) {
            cityRepository.save(city);
            return city.getId();
        } else
            throw new CityAlreadyExistException("Город " + city.getName() + " уже существует");
    }
}
