package com.example.hrautomationbackend.controller;

import com.example.hrautomationbackend.model.PushNotificationRequest;
import com.example.hrautomationbackend.model.PushNotificationResponse;
import com.example.hrautomationbackend.service.PushNotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PushNotificationController {


    private PushNotificationService pushNotificationService;

    public PushNotificationController(PushNotificationService pushNotificationService) {
        this.pushNotificationService = pushNotificationService;
    }

    @PostMapping("/notification")
    public ResponseEntity sendTokenNotification() {
        PushNotificationRequest request = new PushNotificationRequest("Добавлено новое мероприятие!",
                "Приходите на новое мероприятие", "",
                "dFLkfMWgQu-iDJWm1AuL72:APA91bH-TFm7ksHVbd899xu1J5XH1Yh1ONbeu3PDC1ozKs9CbexIgTC-2YrDW0NPjGjP8tHpKXLwMYBGPd8r65N40HQ_EtcPtP28pX7aZhsloxrI4OqICoykb4mOSzc4l3aAMSimOEQj");
        pushNotificationService.sendPushNotificationToToken(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }

}