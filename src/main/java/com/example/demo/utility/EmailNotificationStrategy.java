package com.example.demo.utility;

import com.example.demo.client.SendGridClient;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component("emailNotificationStrategy")
@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {

    private final SendGridClient sendGridClient;
    private final ObjectMapper objectMapper;

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public EmailNotificationStrategy(SendGridClient sendGridClient, ObjectMapper objectMapper) {
        this.sendGridClient = sendGridClient;
        this.objectMapper = objectMapper;
    }

    @Async
    @Override
    @Retry(name = "emailRetry", fallbackMethod = "handleFailure")
    public boolean send(String recipient, String message) {
        try {
            Email from = new Email("rudhra.duraisamy@rootquotient.com");
            Email to = new Email(recipient);
            String subject = "Verification code for login";
            Content content = new Content("text/plain", message);
            Mail mail = new Mail(from, subject, to, content);

            String jsonBody = objectMapper.writeValueAsString(mail);
            String bearerToken = "Bearer " + sendGridApiKey;

            sendGridClient.sendEmail(bearerToken, "application/json", jsonBody);

            log.info("Email sent successfully via Feign to {}", recipient);
            return true;
        } catch (Exception e) {
            log.error("Error sending email via Feign", e);
            return false;
        }
    }
    // Called when retries are exhausted
    private boolean handleFailure(String recipient, String message, Throwable ex) {
        log.error("All retry attempts failed for recipient {} | Error: {}", recipient, ex.getMessage());
        return false;
    }
}
