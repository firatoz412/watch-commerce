package com.watch.commerce.model;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    
    //sepettki itemler hashsette tutulur
    //her bir ürün sete bir kez eklenir wuantity ile miktarı arttırlır
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,orphanRemoval=true)
    @JsonManagedReference
    private Set<CartItem> items = new HashSet<>();

    private BigDecimal totalPrice = BigDecimal.ZERO;


    //sepet her değiştiğinde toplam fiyat güncellensin
    public void updateTotalPrice(){
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

    public int getTotalItemCount() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    

}
