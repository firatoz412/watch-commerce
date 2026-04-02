package com.watch.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Cart;
import com.watch.commerce.model.User;

public interface  CartRepository extends JpaRepository<Cart, Long> {

    void deleteCartById(Long cartId);

    boolean existsByUserId(Long userId);

    Cart getCartByUser(User user);
    
}
