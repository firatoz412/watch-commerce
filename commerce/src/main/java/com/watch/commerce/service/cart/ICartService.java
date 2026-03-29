package com.watch.commerce.service.cart;

import java.math.BigDecimal;
import java.util.Optional;

import com.watch.commerce.model.Cart;
import com.watch.commerce.model.User;

public interface ICartService {
    

    //sepeti id sine göre getir
    Cart getCartById(Long id);

    //sepetti cartId ye göre boşaltır
    void clearCart(Long cartId);

    //sepetteki toplam tutarı hesaplar
    BigDecimal getTotalPrice(Long cartId);

    //bu metot ile sepete yeni bir cart oluşturuyoruz.
    Cart initializeNewCart(User user);

   //userId ye göre cart getir
    Optional<Cart> getCartByUserId(Long userId);





    


}
