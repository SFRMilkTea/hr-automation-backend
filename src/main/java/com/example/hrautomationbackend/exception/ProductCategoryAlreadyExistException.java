package com.example.hrautomationbackend.exception;

public class ProductCategoryAlreadyExistException extends Exception {
    public ProductCategoryAlreadyExistException(String message) {
        super(message);
    }
}