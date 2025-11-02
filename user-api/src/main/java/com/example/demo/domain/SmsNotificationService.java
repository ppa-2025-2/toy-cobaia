package com.example.demo.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// sem visibilidade, package private (default)
// privado do pacote
@Component("sms-notification")
class SmsNotificationService implements NotificationService {

    private static Logger logger = LoggerFactory
            .getLogger(SmsNotificationService.class.getName());

    @Override
    public void sendNotification(String destination, 
                                 String title, 
                                 String body) {

        logger.info("Enviando SMS para {} com o assunto {}",
          destination,
          title
        );
    }
}
