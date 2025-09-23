package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private final EmailService emailService;

    @Autowired
    public OtpService(StringRedisTemplate redisTemplate, EmailService emailService) {
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
    }

    public boolean generateAndSendOtp(String email, String username) throws IOException {
        // generate random 6 digit OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        // send OTP to email
        boolean sent = emailService.sendEmail(email, otp);
        if (sent) {
            // store OTP in Redis with 5 min expiry
            redisTemplate.opsForValue().set("OTP_" + username, otp, 5, TimeUnit.MINUTES);
            log.info("OTP generated and sent for user {}", username);
            return true;
        } else {
            log.warn("Failed to send OTP for user {}", username);
            return false;
        }
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

