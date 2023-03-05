package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.BuildingEntity;
import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.exception.BuildingNotFoundException;
import com.example.hrautomationbackend.exception.CityNotFoundException;
import com.example.hrautomationbackend.exception.RestaurantAlreadyExistException;
import com.example.hrautomationbackend.exception.UndefinedLatitudeException;
import com.example.hrautomationbackend.repository.BuildingRepository;
import com.example.hrautomationbackend.repository.CityRepository;
import com.example.hrautomationbackend.repository.RestaurantRepository;
import com.google.maps.errors.ApiException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class BuildingService {

    private final BuildingRepository buildingRepository;
    private final CityRepository cityRepository;
    private final RestaurantRepository restaurantRepository;
    private final GeocoderService geocoderService;

    public BuildingService(BuildingRepository buildingRepository, CityRepository cityRepository, RestaurantRepository restaurantRepository, GeocoderService geocoderService) {
        this.buildingRepository = buildingRepository;
        this.cityRepository = cityRepository;
        this.restaurantRepository = restaurantRepository;
        this.geocoderService = geocoderService;
    }

    public BuildingEntity checkBuildingExist(String address, String name, Long cityId) throws UndefinedLatitudeException,
            IOException, InterruptedException, ApiException, CityNotFoundException, RestaurantAlreadyExistException {
        BuildingEntity building = new BuildingEntity();
        if (buildingRepository.findByAddress(address) == null) {
            CityEntity city = cityRepository
                    .findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));
            building.setCity(city);
            building.setAddress(address);
            building.setLat(Double.parseDouble(geocoderService.getLat(address)));
            building.setLng(Double.parseDouble(geocoderService.getLng(address)));
            buildingRepository.save(building);
        } else {
            building = buildingRepository.findByAddress(address);
            if (building.getRestaurants().contains(restaurantRepository.findByName(name))) {
                throw new RestaurantAlreadyExistException("Ресторан " + name + " по адресу "
                        + building.getAddress() + " уже существует");
            }
        }
        return building;
    }

    public void deleteBuildingIfEmpty(BuildingEntity building){
        if (building.getRestaurants().size() == 0){
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
