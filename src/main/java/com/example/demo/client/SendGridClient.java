package com.example.demo.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sendgridClient", url = "https://api.sendgrid.com/v3")
public interface SendGridClient {

    @PostMapping(value = "/mail/send", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendEmail(
            @RequestHeader("Authorization") String bearerToken,
            @RequestHeader("Content-Type") String contentType,
            @RequestBody String mailRequestJson
    );
}

