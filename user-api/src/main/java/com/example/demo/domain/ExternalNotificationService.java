package com.example.demo.domain;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Service
public class ExternalNotificationService 
    implements INotificationService {
    
    // Cliente HTTP
    private final RestTemplate http;
    private final String notificationServiceUrl;

    private static Logger logger = LoggerFactory
        .getLogger(ExternalNotificationService.class.getName());

    @PostConstruct
    void postConstruct() {
        logger.info("Inicializando com {}", notificationServiceUrl);
    }

    public ExternalNotificationService(
        RestTemplate http,
        @Value("${notification.service.url}") // sPEL
        String notificationServiceUrl
    ) {
        this.http = http;
        this.notificationServiceUrl = notificationServiceUrl;
    }

    @Override
    @Async
    public void sendNotification(String destination, String title, String body) {
        logger.info("Chamando serviço externo {}", notificationServiceUrl);
        
        http.postForEntity(
            notificationServiceUrl,
            Map.of(
                "recipient", destination,
                "title", title,
                "body", body,
                "media", List.of("mail")
            ),
            Void.class
        );

    }
    
}
