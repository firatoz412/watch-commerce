package com.watch.commerce.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.model.Cart;
import com.watch.commerce.model.Product;
import com.watch.commerce.service.cart.CartService;
import com.watch.commerce.service.product.ProductService;


@Controller
public class ProductController {

    private final CartService cartService;
    private final ProductService productService;

    public ProductController(ProductService productService,CartService cartService){
        this.productService = productService;
        this.cartService = cartService;
    }
    

    @GetMapping("/products")
    public String products(
        @RequestParam(required = false) String brand,
        Model model,
        @AuthenticationPrincipal UserDetails userDetails){
        List<Product> products;
        
        if (brand != null && !brand.isEmpty()) {
            products = productService.getProductsByBrand(brand);
        } else {
            products = productService.getAllProducts();
        }
        Cart cart = null;

        if(userDetails != null){
            cart = cartService.initializeNewCart(userDetails.getUsername());
        }

        model.addAttribute("cart", cart);
        model.addAttribute("products", products);
        model.addAttribute("activeBrand", brand);
        return "products"; 
    }
    
    @GetMapping("/search")
    public String search(@RequestParam String name, Model model) {

        List<Product> products = new ArrayList<>();

        if(!name.trim().isEmpty()){
            products = productService.findByNameContaining(name);
        }

        model.addAttribute("products", products);
        model.addAttribute("activeBrand", null); // filtreyi temizle

        return "products";
    }

    //ürün detay sayfası
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-detail";
    }


    


    


}
