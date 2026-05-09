package com.watch.commerce.service.cart;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.dto.CartItemDto;
import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.dto.ProductImageDto;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.Product;
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
    public CartDto getCartById(Long cartId){
        Cart cart = cartRepository.findById(cartId)
        .orElseThrow(() -> {
            throw new ResourceNotFoundException("cart not found");
        });
        updateTotalPrice(cart);
        return convertToDto(cart);
    }

    @Override
    public CartDto getCartByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Cart cart = cartRepository.getCartByUser(user);

        if (cart == null) {
            return getOrCreate(email);
        }
        updateTotalPrice(cart);//her sepeti çağırdığımızda toplam tutarı güncelliyoruz
        return convertToDto(cart);
    }

    
    @Override
    public CartDto getCartByUser(User user) {
        Cart cart = cartRepository.getCartByUser(user);
        if(cart == null){
            return getOrCreate(user.getEmail());
        }
        updateTotalPrice(cart);
        return convertToDto(cart);
    }



    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        CartDto cart = getCartById(cartId);
        return cart.getTotalPrice();
}

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
            () -> new ResourceNotFoundException("cart not found.")
        );
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);
    }


    @Override
    public CartDto getOrCreate(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));

        Cart existingCart = cartRepository.getCartByUser(user);

        if (existingCart != null) {//eğer userın sepeti var ise ona dön
            updateTotalPrice(existingCart);
            return convertToDto(existingCart);
        }

        //yok ise user için yeni cart oluştur
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setItems(new HashSet<>());
        Cart savedCart = cartRepository.save(cart);
        return convertToDto(savedCart);
    
    }


    @Override
    public Set<CartItem> getItems(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
        () -> new ResourceNotFoundException("cart not found")
        );
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

    public CartDto convertToDto(Cart cart) {
        CartDto dto = new CartDto();

        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setUserEmail(cart.getUser().getEmail());
        dto.setTotalPrice(cart.getTotalPrice());

        List<CartItemDto> itemDtos = cart.getItems().stream().map(item -> {
            CartItemDto itemDto = new CartItemDto();
            ProductDto productDto = new ProductDto();
            Product product = item.getProduct();
            
            
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setPrice(product.getPrice());
            productDto.setCategory(product.getCategory());
            productDto.setBrand(product.getBrand());
            productDto.setDescription(product.getDescription());

            if (product.getImage() != null) {
                ProductImageDto imageDto = new ProductImageDto();
                imageDto.setId(product.getImage().getId());
                imageDto.setImageUrl(product.getImage().getImageUrl());
                
                productDto.setImage(imageDto);
            }
            
            itemDto.setProduct(productDto);
            itemDto.setQuantity(item.getQuantity());
            itemDto.setUnitPrice(item.getUnitPrice());
            itemDto.setTotalPrice(item.getTotalPrice());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDtos);
        return dto;
    }
    


    
    
}
