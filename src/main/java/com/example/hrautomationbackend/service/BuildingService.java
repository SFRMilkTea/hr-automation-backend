package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.BuildingEntity;
import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.repository.BuildingRepository;
import com.example.hrautomationbackend.repository.CityRepository;
import com.google.maps.errors.ApiException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final CityRepository cityRepository;
    private final GeocoderService geocoderService;

    public BuildingService(BuildingRepository buildingRepository, CityRepository cityRepository,
                           GeocoderService geocoderService) {
        this.buildingRepository = buildingRepository;
        this.cityRepository = cityRepository;
        this.geocoderService = geocoderService;
    }

    public BuildingEntity checkBuildingByCoordinates(Double lat, Double lng, Long cityId) throws
            IOException, InterruptedException, ApiException, CityNotFoundException {
        // создание здания
        BuildingEntity building = new BuildingEntity();
        // проверка существует ли здание по таким координатам
        if (buildingRepository.findByLatAndLng(lat, lng) == null) {
            // если не существует, то создаем здание
            CityEntity city = cityRepository
                    .findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));
            building.setCity(city);
            building.setAddress(geocoderService.getAddress(lat, lng));
            building.setLat(lat);
            building.setLng(lng);
            buildingRepository.save(building);
        } else
        // если здание существует, то берем его
        {
            building = buildingRepository.findByLatAndLng(lat, lng);
        }
        return building;
    }

    public BuildingEntity checkBuildingByAddress(String address, Long cityId) throws
            IOException, InterruptedException, ApiException, CityNotFoundException {
        // берем координаты
        Double lat = Double.parseDouble(geocoderService.getLat(address));
        Double lng = Double.parseDouble(geocoderService.getLng(address));
        return checkBuildingByCoordinates(lat, lng, cityId);
    }


    public void deleteBuildingIfEmpty(BuildingEntity building) {
        if (building.getRestaurants().size() == 0) {
            buildingRepository.delete(building);
        }
    }

    public void deleteBuilding(Long id) throws BuildingNotFoundException {
        try {
            buildingRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BuildingNotFoundException("Здание с id " + id + " не найдено");
        }
    }
}
