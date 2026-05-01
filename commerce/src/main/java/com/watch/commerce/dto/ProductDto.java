package com.watch.commerce.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class ProductDto {
 
    private Long id;
    private String name;
    private BigDecimal price;
    private String category;
    private String brand;
    private String description;
    private List<ProductImageDto> images;
    
    
}
