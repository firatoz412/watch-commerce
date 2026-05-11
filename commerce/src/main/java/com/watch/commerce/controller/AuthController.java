package com.watch.commerce.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.watch.commerce.request.RegisterRequest;
import com.watch.commerce.service.auth.AuthService;
import com.watch.commerce.service.city.CityService;




@Controller
public class AuthController {

    private final AuthService authService;
    private final CityService cityService;

    public AuthController(AuthService authService,CityService cityService){
        this.authService = authService;
        this.cityService = cityService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            return "login";
        }

        var authorities = authentication.getAuthorities();
        
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin/dashboard";
            } else if (authority.getAuthority().equals("ROLE_USER")) {
                return "redirect:/user/index";
            }
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
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterRequest request, Model model,Principal principal) {

        if (principal != null) {
            return "redirect:/";
        }
        try {
            authService.register(request);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("registerRequest", new RegisterRequest());
            model.addAttribute("cities", cityService.getAllCities());
            return "register";
        }
    
    }


    @GetMapping("/admin/register")
    public String adminRegister(Model model,Principal principal){
        if (principal == null) {
            return "redirect:/login";
        }
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("registerRequest", new RegisterRequest());
        return "admin-register";
    }

    @PostMapping("/admin/register")
    public String adminRegister(@ModelAttribute RegisterRequest request, Model model,Principal principal) {

        try {
            authService.adminRegister(request);
            return "redirect:/admin/users?success=true";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("registerRequest", request);
            model.addAttribute("cities", cityService.getAllCities());
            return "admin-register";
        }
    }



}
