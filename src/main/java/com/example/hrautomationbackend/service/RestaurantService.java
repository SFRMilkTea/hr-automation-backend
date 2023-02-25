package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.*;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.model.Restaurant;
import com.example.hrautomationbackend.model.RestaurantCard;
import com.example.hrautomationbackend.model.RestaurantResponse;
import com.example.hrautomationbackend.repository.BuildingRepository;
import com.example.hrautomationbackend.repository.CityRepository;
import com.example.hrautomationbackend.repository.RestaurantRepository;
import com.example.hrautomationbackend.repository.RestaurantStatusRepository;
import com.google.maps.errors.ApiException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final BuildingRepository buildingRepository;
    private final RestaurantStatusRepository restaurantStatusRepository;
    private final CityRepository cityRepository;
    private final GeocoderService geocoderService;

    public RestaurantService(RestaurantRepository restaurantRepository, BuildingRepository buildingRepository, RestaurantStatusRepository restaurantStatusRepository, CityRepository cityRepository, GeocoderService geocoderService) {
        this.restaurantRepository = restaurantRepository;
        this.buildingRepository = buildingRepository;
        this.restaurantStatusRepository = restaurantStatusRepository;
        this.cityRepository = cityRepository;
        this.geocoderService = geocoderService;
    }

    public Long addRestaurant(RestaurantResponse restaurantResponse, Long statusId, Long cityId) throws RestaurantAlreadyExistException,
            RestaurantStatusNotFoundException, CityNotFoundException, UndefinedLatitudeException, IOException, InterruptedException, UndefinedLongitudeException, ApiException {

        BuildingEntity building = new BuildingEntity();

        if (buildingRepository.findByAddress(restaurantResponse.getAddress()) == null) {
            CityEntity city = cityRepository
                    .findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));
            building.setCity(city);
            building.setAddress(restaurantResponse.getAddress());
            building.setLat(Double.parseDouble(geocoderService.getLat(restaurantResponse.getAddress())));
            building.setLng(Double.parseDouble(geocoderService.getLng(restaurantResponse.getAddress())));
            buildingRepository.save(building);
        } else {
            building = buildingRepository.findByAddress(restaurantResponse.getAddress());
            if (building.getRestaurants().contains(restaurantRepository.findByName(restaurantResponse.getName()))) {
                throw new RestaurantAlreadyExistException("Ресторан " + restaurantResponse.getName() + " по адресу "
                        + building.getAddress() + " уже существует");
            }
        }

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

    public void deleteRestaurant(Long id) throws RestaurantNotFoundException {
        try {
            RestaurantEntity restaurant = restaurantRepository
                    .findById(id)
                    .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + id + " не найден"));
            restaurant.setAverage(0);
            restaurantRepository.deleteById(id);
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

    public void deleteBuilding(Long id) throws BuildingNotFoundException {
        try {
            buildingRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new BuildingNotFoundException("Здание с id " + id + " не найдено");
        }
    }

}
