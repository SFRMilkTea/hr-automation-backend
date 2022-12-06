package com.example.hrautomationbackend.exception;

public class UserNotAdminException extends Exception {
    public UserNotAdminException(String message) {
        super(message);
    }
}