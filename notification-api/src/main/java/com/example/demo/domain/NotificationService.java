package com.example.demo.domain;

interface NotificationService {
    
    void sendNotification(String destination,
                          String title,
                          String body);
}
