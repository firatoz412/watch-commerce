package com.watch.commerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Comment {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String text;
    private Integer rating;
    
    //birden fazla yorum bir user'a ait olabilir
    @ManyToOne 
    @JoinColumn(name = "user_id")
    private User user;
    
    //bir den fazla yorum bir product'a ait olabilir.
    @ManyToOne 
    @JoinColumn(name = "product_id")
    private Product product;
}
