package com.watch.commerce.response;

import com.watch.commerce.dto.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private OrderDto order;
    private String message;
    private boolean success;
    
}
