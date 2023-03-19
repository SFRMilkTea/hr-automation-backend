package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.*;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.model.Restaurant;
import com.example.hrautomationbackend.model.RestaurantCard;
import com.example.hrautomationbackend.model.RestaurantResponse;
import com.example.hrautomationbackend.model.RestaurantUpdate;
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
    private final RestaurantStatusRepository restaurantStatusRepository;
    private final CityRepository cityRepository;
    private final BuildingService buildingService;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             RestaurantStatusRepository restaurantStatusRepository,
                             CityRepository cityRepository, BuildingService buildingService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantStatusRepository = restaurantStatusRepository;
        this.cityRepository = cityRepository;
        this.buildingService = buildingService;
    }

    public Long addRestaurantByAddress(RestaurantResponse restaurantResponse, Long statusId, Long cityId) throws
            RestaurantStatusNotFoundException, CityNotFoundException, IOException, InterruptedException, ApiException {
        // создание здания
        BuildingEntity building = buildingService.checkBuildingByAddress(restaurantResponse.getAddress(), cityId);
        // создание ресторана
        return addRestaurant(building, restaurantResponse.getName(), statusId);
    }

    public Long addRestaurantByCoordinates(RestaurantResponse restaurantResponse, Long statusId, Long cityId) throws
            RestaurantStatusNotFoundException, CityNotFoundException, IOException, InterruptedException, ApiException {
        // создание здания
        BuildingEntity building = buildingService.checkBuildingByCoordinates(restaurantResponse.getLat(),
                restaurantResponse.getLng(), cityId);
        // создание ресторана
        return addRestaurant(building, restaurantResponse.getName(), statusId);
    }

    public Long addRestaurant(BuildingEntity building, String name, Long statusId) throws RestaurantStatusNotFoundException {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setBuilding(building);
        restaurant.setName(name);
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
        if (!restaurant.getReviews().isEmpty()) {
            for (ReviewEntity review : restaurant.getReviews()) {
                rating = rating + review.getRating();
            }
            rating = rating / restaurant.getReviews().size();
        }
        restaurant.setRating(rating);
        restaurantRepository.save(restaurant);
    }

    public void calculateAverage(RestaurantEntity restaurant) {
        int average = 0;
        int size = 0;
        if (!restaurant.getReviews().isEmpty()) {
            for (ReviewEntity review : restaurant.getReviews()) {
                int reviewAverage = review.getAverage();
                if (reviewAverage != 0) {
                    average = average + reviewAverage;
                    size++;
                }
            }
            average = Math.round(average / size);
        }
        restaurant.setAverage(average);
        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void updateRestaurant(RestaurantUpdate restaurant) throws RestaurantNotFoundException, IOException, InterruptedException,
            ApiException, CityNotFoundException {
        // берем ресторан
        RestaurantEntity restaurantEntity = restaurantRepository
                .findById(restaurant.getId())
                .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + restaurant.getId() + " не найден"));
        BuildingEntity previousBuilding = restaurantEntity.getBuilding();
        // берем город
        Long cityId = restaurantEntity.getBuilding().getCity().getId();
        // берем имя
        restaurantEntity.setName(restaurant.getName());
        // берем здание по адресу
        BuildingEntity building = buildingService.checkBuildingByAddress(restaurant.getAddress(), cityId);
        restaurantEntity.setBuilding(building);
        restaurantEntity.setStatus(restaurantStatusRepository.findByName(restaurant.getStatus()));
        restaurantRepository.save(restaurantEntity);
        // удаляем предыдущее здание, если вдруг оно опустело
        buildingService.deleteBuildingIfEmpty(previousBuilding);
    }

}
