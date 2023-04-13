package com.example.hrautomationbackend.model;

import com.example.hrautomationbackend.entity.EventEntity;

import java.util.Date;

public class Event {

    private Long id;
    private String name;
    private Date date;
    private String address;
    private String pictureUrl;
    private EventFormat format;

    public static Event toModel(EventEntity entity) {
        Event model = new Event();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setDate(entity.getDate());
        model.setAddress(entity.getAddress());
        model.setPictureUrl(entity.getPictureUrl());
        model.setFormat(entity.getFormat());
        return model;
    }

    public Event() {
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

    public EventFormat getFormat() {
        return format;
    }

    public void setFormat(EventFormat format) {
        this.format = format;
    }
}
