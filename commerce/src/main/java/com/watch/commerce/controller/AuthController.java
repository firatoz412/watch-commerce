package com.watch.commerce.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.watch.commerce.request.RegisterRequest;
import com.watch.commerce.service.auth.AuthService;




@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Principal principal){
        if(principal != null){
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(Principal principal){
        if(principal != null){
            return "redirect:/";
        }
        return "home";
    }
    
    @GetMapping("/register")
    public String register(Model model,Principal principal){
        if(principal != null){
            return "redirect:/";
        }
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }


    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, Model model,Principal principal) {

        if(principal != null){
            return "redirect:/";
        }

        try {
            authService.register(request);
            return "redirect:/login?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            return "register";
        }
    }



}
