package com.example.demo.utility;

import com.example.demo.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final EmailNotificationStrategy emailStrategy;

    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        NotificationStrategy strategy = null;

        switch (event.getChannel().toLowerCase()) {
            case "email":
                strategy = emailStrategy;
                break;
            default:
                log.warn("Unsupported channel: {}", event.getChannel());
        }

        if (strategy != null) {
            boolean success = strategy.send(event.getRecipient(), event.getMessage());
            log.info("Notification sent: {}", success);
        }
    }
}

