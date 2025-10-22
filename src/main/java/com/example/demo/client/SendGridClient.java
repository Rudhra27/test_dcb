package com.example.demo.client;


import com.example.demo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "sendGridClient",
        url = "https://api.sendgrid.com/v3",
        configuration = FeignConfig.class
)

public interface SendGridClient {

    @PostMapping(value = "/mail/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendEmail(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("Content-Type") String contentType,
            @RequestBody String mailRequestJson
    );
}

