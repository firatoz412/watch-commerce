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
import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.model.Cart;
import com.watch.commerce.model.User;
import com.watch.commerce.service.cart.CartItemService;
import com.watch.commerce.service.cart.CartService;
import com.watch.commerce.service.product.ProductService;
import com.watch.commerce.service.user.UserService;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;

    public CartController(CartService cartService,CartItemService cartItemService,UserService userService,ProductService productService){
        this.cartService = cartService;
        this.cartItemService = cartItemService;
        this.userService = userService;
        this.productService = productService;
    }


    //sepet sayfasını görüntüle
    @GetMapping
    public String getCart(Model model,Principal principal){

        if(principal == null){
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName());
        CartDto cart = cartService.getCartByUser(user);
       
        model.addAttribute("cart", cart);
        
        return "cart";
    }   

    //sepete ürün ekleme
    @PostMapping("/add")
    public String addItemToCart(Principal principal,
                                @AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam Long productId,
                                @RequestParam(defaultValue="1") int quantity){//bu paramtereler html parametreleri ile aynı olmalı
        if (principal == null) {
            return "redirect:/login";
        }
       
        Cart cart = cartService.initializeNewCart(userDetails.getUsername());
        cartItemService.addItemToCart(cart.getId(), productId, quantity);
        return "redirect:/cart";

    }

    //Sepeti temizle
    @PostMapping("/clear")
    public String clearCart(Model model,Principal principal){
        if(principal == null){
            return "redirect:/login";
        }
       
        Cart cart = cartService.initializeNewCart(principal.getName());
        cartService.clearCart(cart.getId());
        return "redirect:/cart/";
    }


    // Sepetten 1 ürün çıkar (sil butonu)
    @PostMapping("/remove")
    public String removeItem(Model model,Principal principal,@RequestParam Long productId){

        if (principal == null) {
            return "redirect:/login";
        }

        ProductDto pId = productService.getProductById(productId);
        model.addAttribute("prodcutId",pId);
        Cart cart = cartService.initializeNewCart(principal.getName());
        //cartItemService.removeItemFromCart(cart.getId(), productId); cartItem dto ya dönüştürülecek
        
        return "redirect:/cart";
    }

    @PostMapping("/update-quantity")
    public String updateQuantity(@RequestParam Long productId, 
                                 @RequestParam int quantity, 
                                 @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null){
            return "redirect:/login";
        }
        User user = userService.findByEmail(userDetails.getUsername());
        CartDto cart = cartService.getCartByUser(user);
        
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
