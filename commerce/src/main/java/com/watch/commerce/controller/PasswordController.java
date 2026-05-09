package com.watch.commerce.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.service.password_reset.PasswordResetService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class PasswordController {

    private final PasswordResetService passwordResetService;

    //şifresini unutan kullanıcı şifreyi sıfırlama sayfasına gitsin
    @GetMapping("/auth/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password"; // forgot-password.html sayfasını açar
    }

    @PostMapping("/auth/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        try {
            System.out.println("DEBUG: Şifre sıfırlama işlemi başladı. Email: " + email);
            passwordResetService.createPasswordResetToken(email);
            System.out.println("DEBUG: İşlem başarıyla tamamlandı.");
            model.addAttribute("message", "E-posta adresinize sıfırlama linki gönderildi.");
        } catch (Exception e) {
            System.err.println("!!! HATA OLUŞTU !!!");
            e.printStackTrace();
            model.addAttribute("error", "Hata detayı: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }
        return "forgot-password"; // Aynı sayfada mesaj gösterir
    }

    //yeni şifre belirleme formuna yönlendir
    @GetMapping("/auth/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        // Token'ı sayfada gizli bir alanda tutmak için model'e ekliyoruz
        model.addAttribute("token", token);
        return "reset-password"; // reset-password.html sayfasını açar
    }

    
    //şifre güncelle
    @PostMapping("/auth/reset-password")
    public String processResetPassword(@RequestParam String token, 
                                       @RequestParam String password, 
                                       Model model) {
        try {
            passwordResetService.updatePassword(token, password);
            return "redirect:/login?resetSuccess"; // Şifre değişince login'e yönlendir
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "reset-password";
        }
    }
}