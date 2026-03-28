package com.watch.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Cart;

public interface  CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);

    void deleteCartByUserId(Long userId);

    Long existsByUserId(Long userId);
    
}
