package com.example.demo.domain;

import org.springframework.scheduling.annotation.Async;

interface INotificationService {
    
    @Async
    void sendNotification(String destination,
                          String title,
                          String body);
}
