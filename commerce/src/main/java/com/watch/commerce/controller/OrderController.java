package com.watch.commerce.controller;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.watch.commerce.model.Cart;
import com.watch.commerce.model.CartItem;
import com.watch.commerce.model.OrderForm;
import com.watch.commerce.model.User;
import com.watch.commerce.service.cart.CartService;
import com.watch.commerce.service.user.UserService;


@Controller
public class OrderController {
    
    private final UserService userService;
    private final CartService cartService;

    public OrderController(UserService userService,CartService cartService){
        this.userService = userService;
        this.cartService = cartService;
    }
    @GetMapping("/order")
    public String order(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();

        User user = userService.findByEmail(email);
        Cart cart = cartService.getCartByUser(user);

        Set<CartItem> cartItems;
        if(cart == null){
            cartItems = new HashSet<>();
        }else{
            cartItems = cart.getItems();
        }

        cart.updateTotalPrice();
        BigDecimal subTotal = cart.getTotalPrice();
        BigDecimal discount = BigDecimal.ZERO;
        BigDecimal total = subTotal.subtract(discount);

        model.addAttribute("order", new OrderForm());

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartSubTotal", subTotal);
        model.addAttribute("discount", discount);
        model.addAttribute("cartTotal", total);

        return "order";
}

}
