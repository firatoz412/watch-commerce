package com.watch.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.CartItem;

public interface CartItemRepository  extends JpaRepository<CartItem, Long>{
    
}
