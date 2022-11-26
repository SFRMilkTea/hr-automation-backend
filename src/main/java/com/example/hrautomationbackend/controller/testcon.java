package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class testcon {
    @Autowired
    S3Service s3;
    @GetMapping()
    public void test(@RequestPart File file) throws IOException {
        s3.qwerty(file);
    }
}
