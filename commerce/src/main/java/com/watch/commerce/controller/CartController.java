package com.watch.commerce.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.model.Cart;
import com.watch.commerce.service.cart.CartItemService;
import com.watch.commerce.service.cart.CartService;


@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartItemService cartItemService;
    private final CartService cartService;

    public CartController(CartService cartService,CartItemService cartItemService){
        this.cartService = cartService;
        this.cartItemService = cartItemService;
    }



    //sepet sayfasını görüntüle
    @GetMapping("/{cartId}")
    public String getCartById(@PathVariable Long cartId,Model model){
        Cart cart = cartService.getCartById(cartId);
        model.addAttribute("cart", cart);
        model.addAttribute("items", cart.getItems());
        model.addAttribute("totalPrice",cart.getTotalPrice());
        return "cart";
    }   

    //sepete ürün ekleme
    @PostMapping("/add")
    public String addItemToCart(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam(required=false) Long cartId,
                                @RequestParam Long productId,
                                @RequestParam(defaultValue="1") int quantity){//bu paramtereler html parametreleri ile aynı olmalı
        if(cartId == null){
            Cart newCart = cartService.initializeNewCart(userDetails.getUsername());
            cartId = newCart.getId();
        }
        cartItemService.addItemToCart(cartId, productId, quantity);
        return "redirect:/cart/" + cartId;

    }

    //Sepeti temizle
    @PostMapping("/clear")
    public String clearCart(@RequestParam Long cartId){
        cartService.clearCart(cartId);
        return "redirect:/cart/" + cartId;
    }


    // Sepetten 1 ürün çıkar (sil butonu)
    @PostMapping("/remove")
    public String removeItem(@RequestParam Long cartId, @RequestParam Long productId) {
        cartItemService.removeItemFromCart(cartId, productId);
        return "redirect:/cart/" + cartId;
    }

    @PostMapping("/increase")
    public String increaseQuantity(@RequestParam Long cartId,
                                   @RequestParam Long productId,
                                   @RequestParam int quantity){
        cartItemService.updateItemQuantity(cartId, productId, quantity);
        return "redirect:/cart/" + cartId;
    }

    @PostMapping("/decrease")
    public String decreaseQuantity(@RequestParam Long cartId,
                                   @RequestParam Long productId,
                                   @RequestParam int quantity){
        cartItemService.updateItemQuantity(cartId,productId,quantity);
        return "redirect:/cart/" +cartId;

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
