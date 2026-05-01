package com.watch.commerce.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ProductImageDto {

    private Long id;
    private String imageUrl;
    private Long productId;
    
}
