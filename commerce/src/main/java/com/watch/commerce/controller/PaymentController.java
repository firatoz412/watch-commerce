package com.watch.commerce.controller;


import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.model.User;
import com.watch.commerce.request.OrderRequest;
import com.watch.commerce.service.order.OrderService;
import com.watch.commerce.service.user.UserService;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final OrderService orderService;
    private final UserService userService;

    public PaymentController(OrderService orderService,UserService userService){
        this.orderService = orderService;
        this.userService = userService;
    }
    

    @PostMapping("/process")
    public String startPayment(@ModelAttribute("orderRequest") OrderRequest request, Model model) {
        if (request.getCardNumber().length() < 16 || request.getCardNumber() == null) {
            return "redirect:/order?error=invalid_card";
        }

        String fakeSmsCode = "123456"; 
        model.addAttribute("generatedSms", fakeSmsCode);
        model.addAttribute("order", request); // Bilgileri kaybetmemek için geri gönderiyoruz

        return "sms-verify";
    }

    @PostMapping("/verify-sms")
    public String verifySms(@RequestParam String inputCode, 
                            @RequestParam String generatedCode, 
                            @ModelAttribute("orderRequest") OrderRequest request,
                            Principal principal) {
       
        if(principal == null){
            return "redirect:/login";
        }
        if(request.getPhone() != null) {
            request.setPhone(request.getPhone().replaceAll("[^0-9]", ""));
        }

        User user = userService.findByEmail(principal.getName());
                                
        if (inputCode.equals(generatedCode)) {
            orderService.placeOrder(request, user);
            return "redirect:/payment/success";
        }
        
        return "redirect:/payment/failed";
    }


    @GetMapping("/success")
    public String paymentSucces() {
        return "payment-success";
    }

    

}
