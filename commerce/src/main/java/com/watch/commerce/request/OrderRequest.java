package com.watch.commerce.request;
import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderRequest {
    // Sipariş/Adres Bilgileri
    @NotBlank(message="isim boş bırakılamaz")
    private String firstName;

    @NotBlank(message="soyisim boş bırakılamaz")
    private String lastName;

    @NotBlank(message = "Adres boş bırakılamaz")
    private String address;

    @NotBlank(message = "Telefon boş bırakılamaz")
    @Pattern(
            regexp = "^[0-9]{10,11}$",
            message = "Geçerli bir telefon numarası giriniz"
    )
    private String phone;
    
    @NotBlank(message = "Email boş bırakılamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    // Ödeme Bilgileri (Sadece Controller/Sms adımında lazım, DB'ye gitmeyecek)
    @NotBlank(message = "Kart sahibi adı boş bırakılamaz")
    private String cardHolderName;

    @NotBlank(message = "Kart numarası boş bırakılamaz")
    @Pattern(
            regexp = "^[0-9]{16}$",
            message = "Kart numarası 16 haneli olmalıdır"
    )
    private String cardNumber;

    @NotBlank(message = "Son kullanma ayı boş bırakılamaz")
    private String expireMonth;

    @NotBlank(message = "Son kullanma yılı boş bırakılamaz")
    @Pattern(
            regexp = "^[0-9]{2}$",
            message = "Yıl 2 haneli olmalıdır"
    )
    private String expireYear;

    @NotBlank(message = "CVV boş bırakılamaz")
    @Pattern(
            regexp = "^[0-9]{3,4}$",
            message = "CVV 3 veya 4 haneli olmalıdır"
    )
    private String cvv;

    @NotBlank(message = "Toplam fiyat boş olamaz")
    @DecimalMin(value = "0.0", inclusive = false, message = "Toplam fiyat 0'dan büyük olmalıdır")
    private BigDecimal totalPrice;
}