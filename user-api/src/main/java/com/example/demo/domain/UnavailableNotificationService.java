package com.example.demo.domain;

import org.springframework.stereotype.Component;

import com.example.demo.domain.exceptions.NotificationSubsystemUnavailableException;

@Component("unavailable-notification")
class UnavailableNotificationService implements NotificationService {

    @Override
    public void sendNotification(String destination, 
        String title, String body) throws NotificationSubsystemUnavailableException {

       throw new NotificationSubsystemUnavailableException();
    }
}
