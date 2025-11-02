package com.example.demo.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component("mail-notification")
class MailNotificationService implements NotificationService {

    private static Logger logger = LoggerFactory
        .getLogger(MailNotificationService.class.getName());

    private final MailSender mailSender;

    public MailNotificationService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendNotification(String destination, 
        String title, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setTo(destination);
        message.setText(body);
        logger.info("Enviando e-mail para {} assunto {}", 
            destination, 
            title
        );
        mailSender.send(message);
    }
}
