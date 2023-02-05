package com.example.hrautomationbackend.entity;

import javax.persistence.*;

@Entity
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "restaurant_id")
//    private List<RestaurantEntity> restaurants = new ArrayList<>();

//    public List<RestaurantEntity> getRestaurants() {
//        return restaurants;
//    }
//
//    public void setRestaurants(List<RestaurantEntity> restaurants) {
//        this.restaurants = restaurants;
//    }

    public CityEntity() {
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
}
