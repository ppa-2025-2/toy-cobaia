package com.example.demo.domain.exceptions;

public class NotificationSubsystemUnavailableException extends RuntimeException {
  
  public NotificationSubsystemUnavailableException() {
    super("Notification subsystem is currently unavailable.");
  }

  public NotificationSubsystemUnavailableException(String message) {
    super(message);
  }
    
}
