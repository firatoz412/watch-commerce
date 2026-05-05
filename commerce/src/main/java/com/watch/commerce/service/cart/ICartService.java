package com.watch.commerce.service.cart;

import java.math.BigDecimal;
import java.util.Set;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.User;

public interface ICartService {
    

    //sepeti id sine göre getir
    CartDto getCartById(Long id);

   //userId ye göre cart getir
    CartDto getCartByUser(User user);

    //user'ın emailine göre cart'ı getir
    Cart getCartByEmail(String email);

    //sepetti cartId ye göre boşaltır
    void clearCart(Long cartId);

    //sepetteki toplam tutarı hesaplar
    BigDecimal getTotalPrice(Long cartId);

    //bu metot ile sepete yeni bir cart oluşturuyoruz.
    Cart initializeNewCart(String username);

    //carttaki itemleri al
    Set<CartItem> getItems(Long cartId);


}
