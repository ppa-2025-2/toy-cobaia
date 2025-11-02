package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.NotificationDTO;
import com.example.demo.domain.NotificationFacade;

@RestController
public class NotificationController {
    private static Logger logger = LoggerFactory
        .getLogger(NotificationController.class.getName());

    // uma fachada é um front de objetos mais granulares
    private final NotificationFacade notificationFacade;

    public NotificationController(
        NotificationFacade notificationFacade
    ) {
        this.notificationFacade = notificationFacade;
    }

    @ResponseStatus(code = HttpStatus.OK)
    @PostMapping(value = "/api/v1/notification", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void send(
        @RequestBody NotificationDTO notification) {
        logger.info("Sending notification: {}", notification);
        notificationFacade.sendNotification(notification);
    }
}
