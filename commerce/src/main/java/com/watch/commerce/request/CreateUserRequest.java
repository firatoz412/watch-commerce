package com.watch.commerce.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserRequest {


    @NotBlank(message="isim boş girilemez")
    private String firstname;

    @NotBlank(message="soyisim boş girilemez")
    private String lastName;

    @NotBlank(message="email boş girilemez")
    private String email;

    @NotBlank(message="şifre boş girilemez")
    private String password;

    //kayıt sayfasında telefon ekleme yok şimdilik not null eklemiyorum
    private String phone;

    private Long cityId;

    @NotBlank(message="adres boş bırakılamaz")
    private String address;
    
}
