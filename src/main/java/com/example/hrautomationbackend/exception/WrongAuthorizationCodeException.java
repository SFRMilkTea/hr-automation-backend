package com.example.hrautomationbackend.exception;

public class WrongAuthorizationCodeException extends Exception {
    public WrongAuthorizationCodeException(String message) {
        super(message);
    }
}