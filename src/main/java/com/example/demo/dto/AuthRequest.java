package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequest {

    @NotBlank(message = "User Name is mandatory")
    private String username;

    @NotBlank(message = "Password is mandatory")
    private String password;
}
