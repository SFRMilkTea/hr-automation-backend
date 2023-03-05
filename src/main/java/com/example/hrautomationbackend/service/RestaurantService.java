package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.*;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.model.Restaurant;
import com.example.hrautomationbackend.model.RestaurantCard;
import com.example.hrautomationbackend.model.RestaurantResponse;
import com.example.hrautomationbackend.model.RestaurantUpdate;
import com.example.hrautomationbackend.repository.BuildingRepository;
import com.example.hrautomationbackend.repository.CityRepository;
import com.example.hrautomationbackend.repository.RestaurantRepository;
import com.example.hrautomationbackend.repository.RestaurantStatusRepository;
import com.google.maps.errors.ApiException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final BuildingRepository buildingRepository;
    private final RestaurantStatusRepository restaurantStatusRepository;
    private final CityRepository cityRepository;
    private final BuildingService buildingService;

    public RestaurantService(RestaurantRepository restaurantRepository, BuildingRepository buildingRepository, RestaurantStatusRepository restaurantStatusRepository, CityRepository cityRepository, GeocoderService geocoderService, BuildingService buildingService) {
        this.restaurantRepository = restaurantRepository;
        this.buildingRepository = buildingRepository;
        this.restaurantStatusRepository = restaurantStatusRepository;
        this.cityRepository = cityRepository;
        this.buildingService = buildingService;
    }

    public Long addRestaurant(RestaurantResponse restaurantResponse, Long statusId, Long cityId) throws RestaurantAlreadyExistException,
            RestaurantStatusNotFoundException, CityNotFoundException, UndefinedLatitudeException, IOException,
            InterruptedException, ApiException {
        BuildingEntity building = buildingService.checkBuildingExist(restaurantResponse.getAddress(), restaurantResponse.getName(), cityId);
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setBuilding(building);
        restaurant.setName(restaurantResponse.getName());
        RestaurantStatusEntity status = restaurantStatusRepository
                .findById(statusId)
                .orElseThrow(() -> new RestaurantStatusNotFoundException("Статус с id " + statusId + " не найден"));
        restaurant.setStatus(status);
        restaurantRepository.save(restaurant);
        return restaurant.getId();
    }

    public List<Restaurant> getRestaurants(Pageable pageable) {
        Page<RestaurantEntity> restaurants = restaurantRepository.findAll(pageable);
        ArrayList<Restaurant> restaurantsModel = new ArrayList<>();
        for (RestaurantEntity restaurant : restaurants) {
            restaurantsModel.add(Restaurant.toModel(restaurant));
        }
        return restaurantsModel;
    }

    public void deleteRestaurant(Long id) throws RestaurantNotFoundException, BuildingNotFoundException {
        try {
            RestaurantEntity restaurant = restaurantRepository
                    .findById(id)
                    .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + id + " не найден"));
            BuildingEntity building = restaurant.getBuilding();
            restaurantRepository.deleteById(id);
            buildingService.deleteBuildingIfEmpty(building);
        } catch (EmptyResultDataAccessException e) {
            throw new RestaurantNotFoundException("Ресторан с id " + id + " не найден");
        }
    }

    public Long addRestaurantStatus(RestaurantStatusEntity status) throws RestaurantStatusAlreadyExistException {
        if (restaurantStatusRepository.findByName(status.getName()) == null) {
            restaurantStatusRepository.save(status);
            return status.getId();
        } else
            throw new RestaurantStatusAlreadyExistException("Статус " + status.getName() + " уже существует");
    }

    public List<RestaurantStatusEntity> getStatuses() {
        List<RestaurantStatusEntity> list = (List<RestaurantStatusEntity>) restaurantStatusRepository.findAll();
        list.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return list;
    }

    public List<Restaurant> getRestaurantsByStatus(Long statusId) throws RestaurantStatusNotFoundException {
        RestaurantStatusEntity status = restaurantStatusRepository
                .findById(statusId)
                .orElseThrow(() -> new RestaurantStatusNotFoundException("Статус с id " + statusId + " не найден"));
        ArrayList<Restaurant> restaurantsModel = new ArrayList<>();
        for (RestaurantEntity restaurant : status.getRestaurants()) {
            restaurantsModel.add(Restaurant.toModel(restaurant));
        }
        return restaurantsModel;
    }

    public List<BuildingEntity> getRestaurantsByCity(Long cityId) throws CityNotFoundException {
        CityEntity city = cityRepository
                .findById(cityId)
                .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));
        return city.getBuildings();
    }

    public void deleteStatus(Long id) throws RestaurantStatusNotFoundException {
        try {
            restaurantStatusRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RestaurantStatusNotFoundException("Статус с id " + id + " не найден");
        }
    }

    public RestaurantCard getRestaurant(Long id) throws RestaurantNotFoundException {
        return RestaurantCard.toModel(restaurantRepository
                .findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + id + " не найден")));
    }

    public RestaurantUpdate getRestaurantForUpdate(Long id) throws RestaurantNotFoundException {
        return RestaurantUpdate.toModel(restaurantRepository
                .findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + id + " не найден")));
    }

    public List<Restaurant> findByString(Pageable pageable, String str) {
        Page<RestaurantEntity> restaurants = restaurantRepository.findAll(pageable);
        ArrayList<Restaurant> restaurantList = new ArrayList<>();
        for (RestaurantEntity restaurant : restaurants) {
            if (restaurant.getName().toLowerCase().contains(str.toLowerCase()) ||
                    restaurant.getBuilding().getAddress().toLowerCase().contains(str.toLowerCase())) {
                restaurantList.add(Restaurant.toModel(restaurant));
            }
        }
        return restaurantList;
    }

    public void calculateRating(RestaurantEntity restaurant) {
        float rating = 0;
        for (ReviewEntity review : restaurant.getReviews()) {
            rating = rating + review.getRating();
        }
        rating = rating / restaurant.getReviews().size();
        restaurant.setRating(rating);
        restaurantRepository.save(restaurant);
    }

    public void calculateAverage(RestaurantEntity restaurant) {
        int average = 0;
        for (ReviewEntity review : restaurant.getReviews()) {
            average = average + review.getAverage();
        }
        average = Math.round(average / restaurant.getReviews().size());
        restaurant.setAverage(average);
        restaurantRepository.save(restaurant);
    }


    @Transactional
    public void updateRestaurant(RestaurantUpdate restaurant) throws RestaurantNotFoundException,
            RestaurantAlreadyExistException, UndefinedLatitudeException, IOException, InterruptedException,
            ApiException, CityNotFoundException {
        RestaurantEntity restaurantEntity = restaurantRepository
                .findById(restaurant.getId())
                .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + restaurant.getId() + " не найден"));
        Long cityId = restaurantEntity.getBuilding().getCity().getId();
        restaurantEntity.setName(restaurant.getName());
        BuildingEntity building = buildingService.checkBuildingExist(restaurant.getAddress(), restaurant.getName(), cityId);
        restaurantEntity.setBuilding(building);
        restaurantEntity.setStatus(restaurantStatusRepository.findByName(restaurant.getStatus()));
        restaurantRepository.save(restaurantEntity);
    }

}
