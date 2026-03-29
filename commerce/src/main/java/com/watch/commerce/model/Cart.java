package com.watch.commerce.model;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Cart {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    //bir userın bir sepeti olabilir
    @OneToOne 
    @JoinColumn(name = "user_id")
    private User user;
    
    //bir sepette bir den fazla kart item olabilir
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();

    private BigDecimal totalPrice = BigDecimal.ZERO;

    private int quantity;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
        name = "cart_products",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();


    //sepettki itemler hashsette tutulur
    //her bir ürün sete bir kez eklenir wuantity ile miktarı arttırlır
    @OneToMany(mappedBy="cart",cascade=CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    private Set<CartItem> items = new HashSet<>();

    private void updateTotalPrice(){
        this.totalPrice = items.stream().map(item ->{

         BigDecimal unitPrice = item.getUnitPrice();
         if(unitPrice == null){
            return BigDecimal.ZERO;
         }   
         return unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));

        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


     public void addItem(CartItem item){
        this.items.add(item);
        item.setCart(this);
        updateTotalPrice();
    }


    public void removeItem(CartItem item){
        this.items.remove(item);
        item.setCart(this);
        updateTotalPrice();
    }

    

}
