package com.example.hrautomationbackend.entity;

import com.example.hrautomationbackend.model.EventResponse;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(length = 1024)
    private String description;
    @Column(nullable = false)
    private Date date;
    private String address;
    @Column(columnDefinition = "double precision default 0")
    private double lat;
    @Column(columnDefinition = "double precision default 0")
    private double lng;
    private String pictureUrl;
    @Column(columnDefinition = "boolean default false")
    private boolean isOnline;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id")
    private List<EventMaterialEntity> materials = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private CityEntity city;

    public EventEntity() {
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

    public List<EventMaterialEntity> getMaterials() {
        return materials;
    }

    public void setMaterials(List<EventMaterialEntity> materials) {
        this.materials = materials;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public static EventEntity toEntity(EventResponse response, CityEntity city) {
        EventEntity entity = new EventEntity();
        entity.setId(response.getId());
        entity.setName(response.getName());
        entity.setDate(response.getDate());
        entity.setAddress(response.getAddress());
        entity.setOnline(response.isOnline());
        entity.setDescription(response.getDescription());
        entity.setPictureUrl(response.getPictureUrl());
        entity.setCity(city);
        return entity;
    }
}