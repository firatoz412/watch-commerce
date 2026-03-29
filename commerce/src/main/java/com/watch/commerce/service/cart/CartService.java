package com.watch.commerce.service.cart;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

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
        cart.setTotalPrice(calculateTotal(cart));
        return cart;
    }

    @Override
    public Optional<Cart> getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCartById(cartId);
        return cart.getCartItems().stream()
        .map(CartItem :: getTotalPrice)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
}

    @Override
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> {throw new RuntimeException("cart not found");});
        cart.getCartItems().clear();
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


    public BigDecimal calculateTotal(Cart cart) {
        return cart.getCartItems().stream()
            .map(CartItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    
    
}
