package com.watch.commerce.request;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    @NotBlank(message="isim boş bırakılamaz")
    private String firstName;

    @NotBlank(message="soyisim boş bırakılamaz")
    private String lastName;

    @NotBlank(message="email boş bırakılamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message="şifre boş bırakılamaz")
    private String password;

    @NotBlank(message="şifre tekrarı boş bırakılamaz")
    private String confirmPassword;
    
}
