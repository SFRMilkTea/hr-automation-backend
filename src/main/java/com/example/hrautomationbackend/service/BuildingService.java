package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.BuildingEntity;
import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.model.Restaurant;
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

    public BuildingEntity checkBuildingCoordinates(Double lat, Double lng, String name, Long cityId) throws
            IOException, InterruptedException, ApiException, CityNotFoundException, RestaurantAlreadyExistException,
            BuildingAlreadyExistException {
        BuildingEntity building = new BuildingEntity();
        if (buildingRepository.findByLatAndLng(lat, lng) == null) {
            CityEntity city = cityRepository
                    .findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));
            building.setCity(city);
            building.setAddress(geocoderService.getAddress(lat, lng));
            building.setLat(lat);
            building.setLng(lng);
            buildingRepository.save(building);
        } else
            throw new BuildingAlreadyExistException("Здание по таким координатам уже существует. Укажите адрес вот так: " +
                    buildingRepository.findByLatAndLng(lat, lng).getAddress());
        building = buildingRepository.findByAddress(geocoderService.getAddress(lat, lng));
        for (Restaurant restaurant : building.getRestaurants()) {
            if (restaurant.getName().equals(name)) {
                throw new RestaurantAlreadyExistException("Ресторан " + name + " по адресу "
                        + building.getAddress() + " уже существует");
            }
        }
        return building;
    }

    public BuildingEntity checkBuildingExist(String address, String name, Long cityId) throws
            UndefinedLatitudeException,
            IOException, InterruptedException, ApiException, CityNotFoundException, RestaurantAlreadyExistException,
            BuildingAlreadyExistException {
        BuildingEntity building = new BuildingEntity();
        if (buildingRepository.findByAddress(address) == null) {
            Double lat = Double.parseDouble(geocoderService.getLat(address));
            Double lng = Double.parseDouble(geocoderService.getLng(address));
            if (buildingRepository.findByLatAndLng(lat, lng) == null) {
                CityEntity city = cityRepository
                        .findById(cityId)
                        .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));
                building.setCity(city);
                building.setAddress(address);
                building.setLat(lat);
                building.setLng(lng);
                buildingRepository.save(building);
            } else
                throw new BuildingAlreadyExistException("Здание по таким координатам уже существует. Укажите адрес вот так: " +
                        buildingRepository.findByLatAndLng(lat, lng).getAddress());
        } else {
            building = buildingRepository.findByAddress(address);
            for (Restaurant restaurant : building.getRestaurants()) {
                if (restaurant.getName().equals(name)) {
                    throw new RestaurantAlreadyExistException("Ресторан " + name + " по адресу "
                            + building.getAddress() + " уже существует");
                }
            }
        }
        return building;
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
