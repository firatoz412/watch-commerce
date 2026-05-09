package com.watch.commerce.request;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.model.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProductRequest {
    
    @NotBlank(message="ürün ismi boş girilemez")
    private String productName;

    @NotBlank(message="ürün fiyatı bş girilemez")
    private BigDecimal price;

    @Size(min=1)
    @NotBlank(message="güncellenek ürünün miktarı belirtilmelidir.")
    private int stock;

    @NotBlank(message="güncellencek ürünün markası boş girilemez")
    private String brand;

    private String description;

    @NotBlank(message="güncellenecek ürünün bir kategorisi olmalı")
    private Category category;
    //private Long categoryId;
    private MultipartFile productImage;
    
}
