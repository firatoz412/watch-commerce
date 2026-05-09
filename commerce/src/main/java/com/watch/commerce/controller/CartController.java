package com.watch.commerce.controller;

import java.security.Principal;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.model.User;
import com.watch.commerce.service.cart.CartItemService;
import com.watch.commerce.service.cart.CartService;
import com.watch.commerce.service.user.UserService;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;

    public CartController(CartService cartService,CartItemService cartItemService,UserService userService){
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
    }


    //sepet sayfasını görüntüle
    @GetMapping
    public String getCart(Model model,Principal principal){

        if(principal == null){
            return "redirect:/login";
        }

        User user = userService.getUser(principal.getName());
        CartDto cart = cartService.getCartByUser(user);//cart nesnedi dtodur
       
        model.addAttribute("cart", cart);
        
        return "cart";
    }   

    //sepete ürün ekleme
    @PostMapping("/add")
    public String addItemToCart(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam Long productId,
                                @RequestParam(defaultValue="1") int quantity){//bu paramtereler html parametreleri ile aynı olmalı

        if (userDetails == null) {
            return "redirect:/login";
        }
       
        CartDto cart = cartService.getOrCreate(userDetails.getUsername());
        cartItemService.addItemToCart(cart.getId(), productId, quantity);
        return "redirect:/cart";

    }

    //Sepeti temizle
    @PostMapping("/clear")
    public String clearCart(Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
       
        CartDto cart = cartService.getOrCreate(principal.getName());
        cartService.clearCart(cart.getId());
        return "redirect:/cart/";
    }


    // Sepetten 1 ürün çıkar (kaldır butonu)
    @PostMapping("/remove")
    public String removeItem(Principal principal,@RequestParam Long productId){

        if (principal == null) {
            return "redirect:/login";
        }

        cartService.getOrCreate(principal.getName());
        cartItemService.removeItemFromCart(principal.getName(), productId);
        
        return "redirect:/cart";
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(@RequestParam Long productId, 
                                 @RequestParam int quantity, 
                                 @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null){
            return "redirect:/login";
        }
        String email = userDetails.getUsername();
        if (quantity <= 0) {
            cartItemService.removeItemFromCart(email, productId);
        } else {
            cartItemService.updateItemQuantity(email, productId, quantity);
        }
        return "redirect:/cart";
    }

}