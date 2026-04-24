package com.watch.commerce.service.cart;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.CartRepository;
import com.watch.commerce.repository.UserRepository;


@Service
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository){
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    
    }

    @Override
    public Cart getCartById(Long cartId){
        Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> {
            throw new ResourceNotFoundException("cart not found");
        });
        updateTotalPrice(cart);
        return cart;
    }

    @Override
    public Cart getCartByUser(User user) {
        return cartRepository.getCartByUser(user);
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
    public Cart initializeNewCart(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Cart existingCart = cartRepository.getCartByUser(user);

        if (existingCart != null) {//eğer userın sepeti var ise ona dön
            updateTotalPrice(existingCart);
            return existingCart;
        }

        //yok ise user için yeni cart oluştur
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new HashSet<>());
        return cartRepository.save(cart);
    
    }


    @Override
    public Set<CartItem> getItems(Long cartId) {
        Cart cart = getCartById(cartId);
        return cart.getItems();
    }

    //sepet her değiştiğinde toplam fiyat güncellensin
    public void updateTotalPrice(Cart cart){
         BigDecimal cartTotal = cart.getItems().stream()
            .map(item -> item.getUnitPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(cartTotal);
    }


     public void addItem(Cart cart,CartItem item){
        if(cart == null || item == null){
            return;
        }
        cart.getItems().add(item);
        item.setCart(cart);
        updateTotalPrice(cart);
        cartRepository.save(cart);
    }


    public void removeItem(Cart cart,CartItem item){
        if(cart == null || item == null){
            return;
        }
        cart.getItems().remove(item);
        item.setCart(null);
        updateTotalPrice(cart);
        cartRepository.save(cart);
    }

    public int getTotalItemCount(Cart cart) {
        if (cart == null || cart.getItems() == null) {
            return 0;
        }
        return cart.getItems().
        stream().
        mapToInt(CartItem::getQuantity).
        sum();
    }



    
    
}
