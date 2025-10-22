package com.example.demo.service;

import com.example.demo.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public OtpService(StringRedisTemplate redisTemplate, ApplicationEventPublisher eventPublisher) {
        this.redisTemplate = redisTemplate;
        this.eventPublisher = eventPublisher;
    }

    public boolean generateAndSendOtp(String email, String username) {
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        // publish email notification event
        eventPublisher.publishEvent(
                new NotificationEvent(this, "email", email, "OTP for TestDCB login - " + otp)
        );

        // store OTP in Redis
        redisTemplate.opsForValue().set("OTP_" + username, otp, 5, TimeUnit.MINUTES);
        log.info("OTP generated and event published for {}", username);

        return true;
    }

    public boolean verifyOtp(String username, String enteredOtp) {
        String key = "OTP_" + username;
        String storedOtp = redisTemplate.opsForValue().get(key);

        if (storedOtp != null && storedOtp.equals(enteredOtp)) {
            redisTemplate.delete(key);
            return true;
        }
        return false;
    }
}
