package com.watch.commerce.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.service.cart.CartService;


@Controller
public class HomeController {

    private final CartService cartService;

    public HomeController(CartService cartService){
        this.cartService = cartService;
    }
 
    
@GetMapping("/")
public String homePage(Model model, Principal principal) {

    if (principal != null) {
        CartDto cart = cartService.initializeNewCart(principal.getName());
        model.addAttribute("cart", cart);
    } else {
        model.addAttribute("cart", null);
    }
    
    return "HomePage";
}



}
