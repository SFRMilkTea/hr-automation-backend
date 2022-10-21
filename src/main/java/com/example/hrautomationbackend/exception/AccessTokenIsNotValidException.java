package com.example.hrautomationbackend.exception;

public class AccessTokenIsNotValidException extends Exception {
    public AccessTokenIsNotValidException(String message) {
        super(message);
    }
}