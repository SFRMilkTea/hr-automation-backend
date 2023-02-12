package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.entity.CityEntity;
import com.example.hrautomationbackend.entity.RestaurantEntity;
import com.example.hrautomationbackend.entity.RestaurantStatusEntity;
import com.example.hrautomationbackend.exception.*;
import com.example.hrautomationbackend.model.Restaurant;
import com.example.hrautomationbackend.repository.CityRepository;
import com.example.hrautomationbackend.repository.RestaurantRepository;
import com.example.hrautomationbackend.repository.RestaurantStatusRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantStatusRepository restaurantStatusRepository;
    private final CityRepository cityRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, RestaurantStatusRepository restaurantStatusRepository, CityRepository cityRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantStatusRepository = restaurantStatusRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    public Long addRestaurant(RestaurantEntity restaurant, Long statusId, Long cityId) throws RestaurantAlreadyExistException,
            RestaurantStatusNotFoundException, CityNotFoundException {

        if (restaurantRepository.findByNameAndAddress(restaurant.getName(), restaurant.getAddress()) == null) {
            RestaurantStatusEntity status = restaurantStatusRepository
                    .findById(statusId)
                    .orElseThrow(() -> new RestaurantStatusNotFoundException("Статус с id " + statusId + " не найден"));
            restaurant.setStatus(status);
            CityEntity city = cityRepository
                    .findById(cityId)
                    .orElseThrow(() -> new CityNotFoundException("Город с id " + cityId + " не найден"));
            restaurant.setCity(city);
            (Logger.getLogger(RestaurantService.class.getName())).info(
                    "! Hello Arina this is your address ---> " + restaurant.getAddress() );
            restaurantRepository.save(restaurant);
            return restaurant.getId();
        } else
            throw new RestaurantAlreadyExistException("Ресторан " + restaurant.getName() + " по адресу " +
                    restaurant.getAddress() + " уже существует");
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

    public void deleteStatus(Long id) throws RestaurantStatusNotFoundException {
        try {
            restaurantStatusRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new RestaurantStatusNotFoundException("Статус с id " + id + " не найден");
        }
    }

    public RestaurantEntity getRestaurant(Long id) throws RestaurantNotFoundException {
        return restaurantRepository
                .findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Ресторан с id " + id + " не найден"));
    }

}
