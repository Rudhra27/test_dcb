package com.example.demo.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final String channel;
    private final String recipient;
    private final String message;

    public NotificationEvent(Object source, String channel, String recipient, String message) {
        super(source);
        this.channel = channel;
        this.recipient = recipient;
        this.message = message;
    }
}

