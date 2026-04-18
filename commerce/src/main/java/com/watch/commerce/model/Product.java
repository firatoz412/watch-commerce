package com.watch.commerce.model;

import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;
    private int stock;
    
    //birden fazla ürün bir kategoriye ait olabilir
    @ManyToOne 
    @JoinColumn(name = "category_id")
    private Category category;

    private String brand;
    
    //bir ürüne ait birden fazla resim olabilir
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private ProductImage image;

    @Column(length = 1000)
    private String description;


    
}