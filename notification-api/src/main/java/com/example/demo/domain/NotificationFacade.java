package com.example.demo.domain;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.demo.dto.NotificationDTO;

// NotificationFaçade (fachada), é uma fachada para outros serviços
@Service
public class NotificationFacade {

  private static Logger logger = LoggerFactory.getLogger(NotificationFacade.class.getName());

  private final Map<String, NotificationService> notificationServices;

  public NotificationFacade(
      Map<String, NotificationService> notificationServices) {    
    this.notificationServices = notificationServices;
    
    notificationServices.values().forEach(service -> 
      logger.info("Notification Service registrado: {}", service.getClass().getName()));
  }

  
  public void sendNotification(NotificationDTO notification) {

    notification.media().stream()
        .map(media -> media + "-notification")
        .peek(service -> logger.info("Notification Service {} solicitado", service))
        .forEach(service -> {
          if (notificationServices.containsKey(service)) {
            logger.info("Notification Service {} acionado", service);
            notificationServices.get(service)
                .sendNotification(
                    notification.recipient(), notification.title(),
                    notification.body());
          } else {
            logger.warn("Notification Service {} não encontrado", service);
          }
        });
  }
}
