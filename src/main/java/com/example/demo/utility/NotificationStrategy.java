package com.example.demo.utility;

public interface NotificationStrategy {
    boolean send(String recipient, String message);
}
