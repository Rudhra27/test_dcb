package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpVerificationRequest {

    @NotBlank(message = "User Name is mandatory")
    private String username;

    @NotBlank(message = "Otp is mandatory")
    private String otp;
}
