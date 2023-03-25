package com.example.hrautomationbackend.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, columnDefinition = "varchar(255) default 'Без названия'")
    private String name;
    @Column(length = 1024)
    private String description;
    @Column(nullable = false, columnDefinition = "date default '01-01-2000'")
    private Date date;
    @Column(nullable = false, columnDefinition = "varchar(255) default 'Какой-то адрес'")
    private String address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_gallery_id")
    private List<EventGalleryEntity> photos = new ArrayList<>();

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

    public List<EventGalleryEntity> getPhotos() {
        return photos;
    }

    public void setPhotos(List<EventGalleryEntity> photos) {
        this.photos = photos;
    }
}
