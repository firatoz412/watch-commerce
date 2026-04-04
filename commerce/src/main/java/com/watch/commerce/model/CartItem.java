package com.watch.commerce.model;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartItem {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //birden fazla cartItem bir cartta bulunabilir
    @ManyToOne 
    @JoinColumn(name = "cart_id")
    @JsonBackReference
    private Cart cart;
    
    //birden fazla cartItem aynı product a sahip olabilir
    @ManyToOne 
    @JoinColumn(name = "product_id")
    private Product product;
    private Integer quantity;

    private BigDecimal unitPrice;
    private BigDecimal totalPrice;



    public void setTotalPrice(){
        this.totalPrice = this.unitPrice.multiply(new BigDecimal(quantity));
    }
}
