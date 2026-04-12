package com.watch.commerce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.watch.commerce.model.Cart;
import com.watch.commerce.model.OrderForm;


@Controller
public class OrderController {
    

    @GetMapping("/order")
    public String order(Model model){
        model.addAttribute("order", new OrderForm());
        model.addAttribute("cart",new Cart());
        return "order";
    } 

}
