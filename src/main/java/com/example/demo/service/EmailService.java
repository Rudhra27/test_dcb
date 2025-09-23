package com.example.demo.service;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class EmailService {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public boolean sendEmail(String email, String otp) throws IOException {
        Email from = new Email("rudhra.duraisamy@rootquotient.com1");
        Email to = new Email(email);

        String subject = "Verification code for login";
        Content content = new Content("text/plain", "OTP for TestDCB login - " + otp);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send"); request.setBody(mail.build());

            Response response = sg.api(request);
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                log.info("Email sent successfully to {}", email);
                return true;
            } else {
                log.error("Failed to send email to {} | Status: {} | Body: {}",
                        email, response.getStatusCode(), response.getBody());
                return false;
            }
        } catch (IOException e) {
            log.error("I/O error while sending email to {}", email, e);
            return false;
        }

    }
}