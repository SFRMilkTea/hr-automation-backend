package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.RestaurantEntity;

public class RestaurantUpdate {
    private Long id;
    private String address;
    private String name;
    private String status;

    public RestaurantUpdate() {
    }

    public static RestaurantUpdate toModel(RestaurantEntity entity) {
        RestaurantUpdate model = new RestaurantUpdate();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setAddress(entity.getBuilding().getAddress());
        model.setStatus(entity.getStatus().getName());
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
