package com.example.hrautomationbackend.exception;

public class RestaurantAlreadyExistException extends Exception {
    public RestaurantAlreadyExistException(String message) {
        super(message);
    }
}