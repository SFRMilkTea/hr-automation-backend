package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.exception.CityAlreadyExistException;
import com.example.hrautomationbackend.exception.CityNotFoundException;
import com.example.hrautomationbackend.repository.CityRepository;
import com.google.maps.errors.ApiException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;
    private final GeocoderService geocoderService;

    public CityService(CityRepository cityRepository, GeocoderService geocoderService) {
        this.cityRepository = cityRepository;
        this.geocoderService = geocoderService;
    }

    public Long addCity(CityEntity city) throws CityAlreadyExistException, IOException, InterruptedException, ApiException {
        if (cityRepository.findByName(city.getName()) == null) {
            city.setLat(Double.parseDouble(geocoderService.getLat(city.getName())));
            city.setLng(Double.parseDouble(geocoderService.getLng(city.getName())));
            cityRepository.save(city);
            return city.getId();
        } else
            throw new CityAlreadyExistException("Город " + city.getName() + " уже существует");
    }

    public List<CityEntity> getCities() {
        List<CityEntity> list = (List<CityEntity>) cityRepository.findAll();
        list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return list;
    }

    public void deleteCity(Long id) throws CityNotFoundException {
        try {
            cityRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new CityNotFoundException("Город с id " + id + " не найден");
        }
    }

}
