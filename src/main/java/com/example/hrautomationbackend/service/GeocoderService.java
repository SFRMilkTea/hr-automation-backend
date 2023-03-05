package com.example.hrautomationbackend.service;

import com.example.hrautomationbackend.exception.UndefinedLatitudeException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeocoderService {

    public GeocoderService() {
    }


    // здесь можно подумать над оптимизацией

    public String getLat(String address) throws IOException, InterruptedException, UndefinedLatitudeException, ApiException {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0x8OjD9BWDrSPy2GDApSqt_pChbbCYQU")
                .build();
        GeocodingResult[] results = GeocodingApi.geocode(context,
                address).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String lat = gson.toJson(results[0].geometry.location.lat);
        context.shutdown();
        return lat;
    }

    public String getLng(String address) throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0x8OjD9BWDrSPy2GDApSqt_pChbbCYQU")
                .build();
        GeocodingResult[] results = GeocodingApi.geocode(context,
                address).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String lng = gson.toJson(results[0].geometry.location.lng);
        context.shutdown();
        return lng;

    }

    public String getAddress(Double lat, Double lng) throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0x8OjD9BWDrSPy2GDApSqt_pChbbCYQU")
                .build();
        GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(lat, lng)).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String address = gson.toJson(results[0].formattedAddress);
        context.shutdown();
        return address;
    }

}

