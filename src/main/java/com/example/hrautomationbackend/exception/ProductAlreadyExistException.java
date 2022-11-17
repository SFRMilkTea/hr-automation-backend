package com.example.hrautomationbackend.exception;

public class ProductAlreadyExistException extends Exception {
    public ProductAlreadyExistException(String message) {
        super(message);
    }
}