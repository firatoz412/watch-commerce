package com.watch.commerce.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.watch.commerce.model.User;
import com.watch.commerce.service.user.UserService;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hesabim")
    public String hesabim(Principal principal, Model model) {

        if(principal == null){
            return "redirect:/login";
        }

        String email = principal.getName(); 
        User user = userService.getUser(email);
        model.addAttribute("user", user);
        return "hesabim";
    }
}
