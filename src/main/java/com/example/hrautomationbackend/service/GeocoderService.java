package com.example.hrautomationbackend.service;

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

    //    final String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json";// путь к Geocoding API по HTTP
//    final String url = baseUrl.concat("?language=ru") ;// генерируем путь с параметрами
//    final JSONObject response = JsonReader.read(url);// делаем запрос к вебсервису и получаем от него ответ
//    // как правило наиболее подходящий ответ первый и данные о координатах можно получить по пути
//    // //results[0]/geometry/location/lng и //results[0]/geometry/location/lat
//    JSONObject location = response.getJSONArray("results").getJSONObject(0);
//    location = location.getJSONObject("geometry");
//    location = location.getJSONObject("location");
//    final double lng = location.getDouble("lng");// долгота
//    final double lat = location.getDouble("lat");// широта
//	System.out.println(String.format("%f,%f", lat, lng));// итоговая широта и долгота
//
    public GeocoderService() {
    }

//    public GeoApiContext getContext() {
//        GeoApiContext context = new GeoApiContext.Builder()
//                .apiKey("AIzaSyD0x8OjD9BWDrSPy2GDApSqt_pChbbCYQU")
//                .build();
//        return context;
//    }

    public String getLat(String address) throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0x8OjD9BWDrSPy2GDApSqt_pChbbCYQU")
                .build();
//        GeoApiContext context = getContext();
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String lat = gson.toJson(results[0].geometry.location.lat);
        context.shutdown();
        return lat;
    }

    public String getLng(String address) throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0x8OjD9BWDrSPy2GDApSqt_pChbbCYQU")
                .build();
//        GeoApiContext context = getContext();
        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String lng = gson.toJson(results[0].geometry.location.lng);
        context.shutdown();
        return lng;
    }

    public String getAddress(Double lat, Double lng) throws IOException, InterruptedException, ApiException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyD0x8OjD9BWDrSPy2GDApSqt_pChbbCYQU")
                .build();
        //        GeoApiContext context = getContext();
        GeocodingResult[] results = GeocodingApi.reverseGeocode(context, new LatLng(lat, lng)).await();
        String address = results[0].formattedAddress;
        context.shutdown();
        return address;
    }

}
