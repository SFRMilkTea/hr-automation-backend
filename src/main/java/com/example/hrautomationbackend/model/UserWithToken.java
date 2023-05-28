package com.example.hrautomationbackend.model;

public class UserWithToken {
    private Long id;
    private String token;

    public UserWithToken() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
