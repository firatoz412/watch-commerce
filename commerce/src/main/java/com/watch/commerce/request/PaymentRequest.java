package com.watch.commerce.request;

import lombok.Data;

@Data
public class PaymentRequest {
    
    private String cardHolderName;
    private String cardNumber;
    private String expireMonth;
    private String expireYear;
    private String cvv;  


}
