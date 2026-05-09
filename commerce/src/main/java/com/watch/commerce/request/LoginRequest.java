package com.watch.commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message="email boş girilemez")
    private String email;

    @NotBlank(message="şifre boş girilemez")
    private String password;
}