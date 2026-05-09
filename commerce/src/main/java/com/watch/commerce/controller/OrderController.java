package com.watch.commerce.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.dto.OrderDto;
import com.watch.commerce.model.Order;
import com.watch.commerce.model.User;
import com.watch.commerce.request.OrderRequest;
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

        User user = userService.getUser(principal.getName());
        CartDto cart = cartService.getCartByUser(user);

        //user'ın sepeti boş ise sipariş olmaz
        if (cart == null || cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        model.addAttribute("order", new OrderRequest());
        model.addAttribute("cartItems", cart.getItems());
        model.addAttribute("cartTotal", cart.getTotalPrice());

        return "order";
    }

    @PostMapping("/order/complete")
    public String processOrder(@ModelAttribute("orderForm") OrderRequest form, 
                               Principal principal,
                               Model model
                            ) {
        if (principal == null){
            return "redirect:/login";
        }
        
        User user = userService.getUser(principal.getName());

        orderService.placeOrder(form,user);

        model.addAttribute("order", new Order());//order = orderDto alanları
        return "redirect:/order/success";
    }


    @GetMapping("/order/my-orders")
    public String myOrders(Model model, Principal principal) {
        if(principal == null){
            return "redirect:/login";
        }
        User user = userService.getUser(principal.getName());
        List<OrderDto> orders = orderService.getUserOrders(user);
        model.addAttribute("orders", orders);
        return "my-orders";
    }



}
