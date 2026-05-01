package com.watch.commerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemDto {
    
    private Long productId;

    private String productName;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal priceAtPurchase;

    private BigDecimal totalPrice;
}
