package com.example.hrautomationbackend.exception;

public class ProductAlreadyOrderedException extends Exception {
    public ProductAlreadyOrderedException(String message) {
        super(message);
    }
}