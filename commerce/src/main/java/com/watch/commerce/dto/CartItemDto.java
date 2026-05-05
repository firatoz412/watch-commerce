package com.watch.commerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
    
    private Long id;
    private ProductDto product;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
