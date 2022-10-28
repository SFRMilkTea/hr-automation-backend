package com.example.hrautomationbackend.service;

import org.springframework.stereotype.Service;

@Service
public class CodeGenerationService {


    int generateCode() {
        int min = 1000;
        int max = 9999;
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
