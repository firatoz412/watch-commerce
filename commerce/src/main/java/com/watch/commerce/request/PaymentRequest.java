package com.watch.commerce.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PaymentRequest {
    
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


}
