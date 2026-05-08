package com.watch.commerce.dto;

import lombok.Data;


@Data
public class ProductImageDto {

    private Long id;
    private String imageUrl;
    private Long productId;
    
}
