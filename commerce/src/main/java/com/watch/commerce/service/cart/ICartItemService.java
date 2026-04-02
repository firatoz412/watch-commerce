package com.watch.commerce.service.cart;

import com.watch.commerce.model.CartItem;

public interface  ICartItemService {
    
    //sepete item ekleme
    void addItemToCart(String username,Long cartId,Long productId,int quantity);

    //sepetteki ürünü kaldırma
    void removeItemFromCart(Long cartId,Long productId);

    //sepetteki itemin miktarını(quantity) güncelleme
    void updateItemQuantity(Long cartId,Long productId,int quantity);

    //sepetteki ürünü getirme
    CartItem getCartItem(Long cartId,Long productId);
}
