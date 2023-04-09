package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.EventEntity;
import com.example.hrautomationbackend.entity.EventMaterialEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventFull {
    private Long id;
    private String name;
    private String description;
    private Date date;
    private String address;
    private String pictureUrl;
    private boolean isOnline;
    private List<String> materials = new ArrayList<>();
    private String city;

    public static EventFull toModel(EventEntity entity) {
        EventFull model = new EventFull();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDescription(entity.getDescription());
        model.setDate(entity.getDate());
        model.setAddress(entity.getAddress());
        model.setPictureUrl(entity.getPictureUrl());
        model.setOnline(entity.isOnline());
        if (entity.getCity() != null) {
            model.setCity(entity.getCity().getName());
        }
        List<String> materials = new ArrayList<>();
        for (EventMaterialEntity material : entity.getMaterials()) {
            materials.add(material.getUrl());
        }
        model.setMaterials(materials);
        return model;
    }

    public EventFull() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public List<String> getMaterials() {
        return materials;
    }

    public void setMaterials(List<String> materials) {
        this.materials = materials;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
