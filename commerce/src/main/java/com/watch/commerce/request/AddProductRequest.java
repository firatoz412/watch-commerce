package com.watch.commerce.request;
import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddProductRequest {
    
    @NotBlank(message = "Ürün adı boş olamaz.")
    private String name;

    @NotBlank(message= "Marka boş olamaz.")
    private String brand;
    
    @NotNull(message = "Fiyat boş olamaz")
    @Positive(message = "Fiyat 0'dan büyük olmalı")
    private BigDecimal price;

    @Min(value = 0, message = "Stok negatif olamaz")
    private int stock;

    private String description;
    
    @NotNull(message = "Ürün için bir kategori seçilmelidir.")
    private Long categoryId;
    
    private String imageUrl;

    
}
