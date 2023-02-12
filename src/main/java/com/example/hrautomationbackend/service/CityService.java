package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.exception.CityAlreadyExistException;
import com.example.hrautomationbackend.exception.CityNotFoundException;
import com.example.hrautomationbackend.repository.CityRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

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
