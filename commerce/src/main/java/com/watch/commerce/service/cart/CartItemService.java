package com.watch.commerce.service.cart;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.dto.CartItemDto;
import com.watch.commerce.exception.ProductNotFoundException;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.Product;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.CartRepository;
import com.watch.commerce.repository.ProductRepository;
import com.watch.commerce.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CartItemService implements ICartItemService {

    private final CartService cartService;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartItemService(CartRepository cartRepository,
                           ProductRepository productRepository,
                           CartService cartService,
                        UserRepository userRepository
                         ) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addItemToCart(Long cartId, Long productId, int quantity) {
      
        if (quantity <= 0) {
            throw new IllegalArgumentException("Miktar 0'dan büyük olmalıdır.");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Sepet bulunamadı."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Ürün bulunamadı."));

        addOrUpdateItem(cart, product, quantity);
        cartService.updateTotalPrice(cart);
        cartRepository.save(cart);
    }

    private void addOrUpdateItem(Cart cart, Product product, int quantity) {
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElseGet(() -> createNewCartItem(cart, product));

        if (item.getId() != null) { 
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            item.setQuantity(quantity);
            cart.getItems().add(item);
        }

        item.setUnitPrice(product.getPrice());
        item.setTotalPrice();
    }

    private CartItem createNewCartItem(Cart cart, Product product) {
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        return newItem;
    }

    @Override
    public void updateItemQuantity(String email, Long productId, int quantity) {

        User user = userRepository.findByEmail(email).orElseThrow(
            () -> new ResourceNotFoundException("email not found")
        );
        Cart cart = cartRepository.getCartByUser(user);
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must be > 0");
        }

        if (quantity == 0) {
            removeItemFromCart(user.getEmail(), productId);
            return;
        }

      

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("cart item not found"));

        item.setQuantity(quantity);
        BigDecimal unitPrice = item.getProduct().getPrice();
        item.setUnitPrice(unitPrice);
        item.setTotalPrice(unitPrice.multiply(new BigDecimal(item.getQuantity())));

        cartService.updateTotalPrice(cart);
        cartRepository.save(cart);
    }

    @Override
    public CartItemDto getCartItem(Long cartId, Long productId) {

        CartDto cart = cartService.getCartById(cartId);

        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("item not found."));
    }

    @Override
    public void updateItemQuantityInCart(String email, Long productId, int quantity) {
        User user = userRepository.findByEmail(email).orElseThrow(
            () -> new ResourceNotFoundException("email not found")
        );
        Cart cart = cartRepository.getCartByUser(user);
    
        if (quantity <= 0) {
            removeItemFromCart(email, productId);
        } else {
            updateItemQuantity(user.getEmail(), productId, quantity);
        }
        cartService.updateTotalPrice(cart);
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeItemFromCart(String email, Long productId) {
       User user = userRepository.findByEmail(email).orElseThrow(
        () -> new ResourceNotFoundException("email not found")
       );

       Cart cart = cartRepository.getCartByUser(user);
       CartItem itemToRemove = cart.getItems().stream()
            .filter(item -> item.getProduct().getId().equals(productId)).
            findFirst().orElseThrow(() -> new ResourceNotFoundException("items not found"));
        
       cart.getItems().remove(itemToRemove);
       cartService.updateTotalPrice(cart);
       cartRepository.save(cart);
    }
}
