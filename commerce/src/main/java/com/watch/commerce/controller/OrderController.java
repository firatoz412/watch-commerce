package com.watch.commerce.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.watch.commerce.model.Cart;
import com.watch.commerce.model.Order;
import com.watch.commerce.model.OrderForm;
import com.watch.commerce.model.User;
import com.watch.commerce.service.cart.CartService;
import com.watch.commerce.service.order.OrderService;
import com.watch.commerce.service.user.UserService;


@Controller
public class OrderController {
    
    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;

    public OrderController(UserService userService,
                          CartService cartService,
                          OrderService orderService){
        this.userService = userService;
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping("/order")
    public String showOrderPage(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        User user = userService.findByEmail(principal.getName());
        Cart cart = cartService.getCartByUser(user);

        //user'ın sepeti boş ise sipariş olmaz
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("order", new Order());
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("cartTotal", cart.getTotalPrice());

        return "order";
    }

    @PostMapping("/order/complete")
    public String processOrder(@ModelAttribute("orderForm") OrderForm form, 
                               Principal principal) {
        if (principal == null){
            return "redirect:/login";
        }
        
        User user = userService.findByEmail(principal.getName());
        
        // Siparişi kaydetme metodunu çağıralım ve ürünü kaydedelim
        orderService.placeOrder(
            user, 
            form.getFirstName(), 
            form.getLastName(), 
            form.getEmail(), 
            form.getPhone(), 
            form.getAddress(), 
            "CREDIT_CARD" //şimdilik sabit ödeme metodu olsun 
        );

        return "redirect:/order/success";
    }



}
