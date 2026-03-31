package com.watch.commerce.service.cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.CartRepository;


@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository){
        this.cartRepository = cartRepository;
    
    }

    @Override
    public Cart getCartById(Long cartId){
        Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> {
            throw new ResourceNotFoundException("cart not found");
        });
        cart.updateTotalPrice();
        return cart;
    }

    @Override
    public Optional<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCartById(cartId);
        return cart.getTotalPrice();
}

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = getCartById(cartId);
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }


    @Override
    public Cart initializeNewCart(User user) {
        return getCartByUserId(user.getId())
            .orElseGet(() -> {
                Cart cart = new Cart();
                cart.setUser(user);
                return cartRepository.save(cart);
            });
    }


    @Override
    public Set<CartItem> getItems(Long cartId) {
        Cart cart = getCartById(cartId);
        return cart.getItems();
    }

    
    
}
