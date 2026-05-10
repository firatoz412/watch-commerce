package com.watch.commerce.controller;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.service.cart.CartService;
import com.watch.commerce.service.favorite.FavoriteService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final CartService cartService;

    public FavoriteController(FavoriteService favoriteService, CartService cartService) {
        this.favoriteService = favoriteService;
        this.cartService = cartService;
    }

    // Favori listesi sayfası
    @GetMapping
    public String getFavorites(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        String email = userDetails.getUsername();
        List<ProductDto> favorites = favoriteService.getFavorites(email);
        Set<Long> favoriteProductIds = favoriteService.getFavoriteProductIds(email);
        CartDto cart = cartService.getOrCreate(email);

        model.addAttribute("favorites", favorites);
        model.addAttribute("favoriteProductIds", favoriteProductIds);
        model.addAttribute("cart", cart);
        return "favorites";
    }

    // Favorilere ürün ekle
    @PostMapping("/add")
    public String addFavorite(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam Long productId,
                              HttpServletRequest request) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        favoriteService.addFavorite(userDetails.getUsername(), productId);

        // Kullanıcıyı geldiği sayfaya geri yönlendir
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/products");
    }

    // Favorilerden ürün sil
    @PostMapping("/remove")
    public String removeFavorite(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam Long productId,
                                 HttpServletRequest request) {
        if (userDetails == null) {
            return "redirect:/login";
        }

        favoriteService.removeFavorite(userDetails.getUsername(), productId);

        // Kullanıcıyı geldiği sayfaya geri yönlendir
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/favorites");
    }

}
