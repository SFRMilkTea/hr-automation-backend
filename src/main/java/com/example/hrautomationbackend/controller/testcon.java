package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/test")
public class testcon {
    @Autowired
    S3Service s3;
    @PostMapping()
    public void test(@RequestPart MultipartFile file) throws IOException {
        File tempFile = s3.createSampleFile(file);
        file.transferTo(tempFile);
        s3.qwerty(tempFile);
    }
}
