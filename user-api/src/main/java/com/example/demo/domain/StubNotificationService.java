package com.example.demo.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

// @Service
// @Profile("development")
public class StubNotificationService
    implements INotificationService {

    @PostConstruct
    void setup() {
        logger.info("Serviço de Notificação Stub registrado");
    }

    private static Logger logger = LoggerFactory.getLogger(StubNotificationService.class.getName());

    @Override
    public void sendNotification(String destination,
                                 String title,
                                 String body) {
        
        logger.info("Enviando notificação para {} com o título {}", destination, title);

    }
    
}
