package com.watch.commerce.service.cart;

import com.watch.commerce.dto.CartItemDto;

public interface  ICartItemService {
    
    //sepete item ekleme
    //void addItemToCart(String email,Long cartId,Long productId,int quantity);
    void addItemToCart(Long cartId,Long productId,int quantity);

    //sepetteki ürünü kaldırma
    void removeItemFromCart(String email,Long productId);

    //sepetteki itemin miktarını(quantity) güncelleme
    void updateItemQuantity(String email,Long productId,int quantity);

    //sepetteki ürün miktarını güncelle
    void updateItemQuantityInCart(String email, Long productId, int quantity);

    //sepetteki ürünü getirme
    CartItemDto getCartItem(Long cartId,Long productId);


}
