package com.watch.commerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class OrderDto {

    private Long id;

    private String status;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private List<OrderItemDto> items;




}
