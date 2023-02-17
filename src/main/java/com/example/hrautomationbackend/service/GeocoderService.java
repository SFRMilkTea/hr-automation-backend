package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.exception.UndefinedLatitudeException;
import com.example.hrautomationbackend.exception.UndefinedLongitudeException;
import com.example.hrautomationbackend.geocoder.Geocoder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocoderService {

    private final ObjectMapper mapper;
    private final Geocoder geocoder;


    public GeocoderService(ObjectMapper mapper, Geocoder geocoder) {
        this.mapper = mapper;
        this.geocoder = geocoder;
    }


    // здесь можно подумать над оптимизацией

    public Long getLat(String address) throws IOException, InterruptedException, UndefinedLatitudeException {
        String response = geocoder.GeocodeSync(address);
        JsonNode responseJsonNode = mapper.readTree(response);
        JsonNode items = responseJsonNode.get("items");

        Long lat = null;
        for (JsonNode item : items) {
            JsonNode position = item.get("position");
            lat = position.get("lat").asLong();
        }
        if (lat != null) {
            return lat;
        } else throw new UndefinedLatitudeException("Невозможно определить широту для адреса " + address);
    }

    public Long getLng(String address) throws IOException, InterruptedException, UndefinedLongitudeException {
        String response = geocoder.GeocodeSync(address);
        JsonNode responseJsonNode = mapper.readTree(response);
        JsonNode items = responseJsonNode.get("items");

        Long lng = null;
        for (JsonNode item : items) {
            JsonNode position = item.get("position");
            lng = position.get("lng").asLong();
        }
        if (lng != null) {
            return lng;
        } else throw new UndefinedLongitudeException("Невозможно определить долготу для адреса " + address);
    }
}

