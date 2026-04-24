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

import com.watch.commerce.model.Cart;
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
    public String getCartById(Model model,Principal principal){

        if(principal == null){
            return "redirect:/login";
        }

        Cart cart = cartService.initializeNewCart(principal.getName());
        if (cart == null) {
            model.addAttribute("cart", new Cart());
        } else {
            model.addAttribute("cart", cart);
        }
        return "cart";
    }   

    //sepete ürün ekleme
    @PostMapping("/add")
    public String addItemToCart(Principal principal,
                                @AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(required=false) Long cartId,
                                @RequestParam Long productId,
                                @RequestParam(defaultValue="1") int quantity){//bu paramtereler html parametreleri ile aynı olmalı
        if (principal == null) {
            return "redirect:/login";
        }
        if(cartId == null){
            Cart newCart = cartService.initializeNewCart(userDetails.getUsername());
            cartId = newCart.getId();
        }
        cartItemService.addItemToCart(cartId, productId, quantity);
        return "redirect:/cart";

    }

    //Sepeti temizle
    @PostMapping("/clear")
    public String clearCart(@RequestParam Long cartId){
        cartService.clearCart(cartId);
        return "redirect:/cart/" + cartId;
    }


    // Sepetten 1 ürün çıkar (sil butonu)
    @PostMapping("/remove")
    public String removeItem(@RequestParam Long productId,Principal principal){

        if (principal == null) {
            return "redirect:/login";
        }
        User user = userService.findByEmail(principal.getName());
        Cart cart = cartService.getCartByUser(user);
        
        if(cart != null){
            cartItemService.removeItemFromCart(cart.getId(), productId);
        }
        return "redirect:/cart";
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(@RequestParam Long productId, 
                                 @RequestParam int quantity, 
                                 Principal principal) {

        if (principal == null){
            return "redirect:/login";
        }

        Cart cart = cartService.initializeNewCart(principal.getName());
        
        if (quantity <= 0) {
            cartItemService.removeItemFromCart(cart.getId(), productId);
        } else {
            cartItemService.updateItemQuantity(cart.getId(), productId, quantity);
        }
        return "redirect:/cart";
    }

    

   

  
    
}


// //sepete ürün ekleme
//     @PostMapping("/add")
//     public String addItemToCart(@AuthenticationPrincipal UserDetails userDetails,
//                                 @RequestParam(required=false) Long cartId,
//                                 @RequestParam Long productId,
//                                 @RequestParam(defaultValue="1") int quantity){//bu paramtereler html parametreleri ile aynı olmalı
//         if(cartId == null){
//             Cart newCart = cartService.initializeNewCart(userDetails.getUsername());
//             cartId = newCart.getId();
//         }
//         cartItemService.addItemToCart(userDetails.getUsername(),cartId, productId, quantity);
//         return "redirect:/cart/" + cartId;

//     }
