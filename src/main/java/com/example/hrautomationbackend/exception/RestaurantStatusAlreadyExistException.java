package com.example.hrautomationbackend.exception;

public class RestaurantStatusAlreadyExistException extends Exception {
    public RestaurantStatusAlreadyExistException(String message) {
        super(message);
    }
}