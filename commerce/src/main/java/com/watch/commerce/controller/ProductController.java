package com.watch.commerce.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.dto.CartDto;
import com.watch.commerce.dto.ProductDto;
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
    public String getProducts(
        @RequestParam(required = false) String keyword,
        Model model,
        @AuthenticationPrincipal UserDetails userDetails){
        
        List<ProductDto> products;
        
        if(keyword != null && !keyword.trim().isEmpty()){
            products = productService.searchProducts(keyword);
        }else{
            products = productService.getAllProducts();
        }

        
        CartDto cart = null;
        if(userDetails != null){
            cart = cartService.initializeNewCart(userDetails.getUsername());
        }

        model.addAttribute("cart", cart);//burdaki cart dtodur;
        model.addAttribute("products", products);
        model.addAttribute("activeBrand", keyword);
        return "products"; 
    }
    
    //ürün detay sayfası
    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductDto productDto = productService.getProductById(id);
        model.addAttribute("productDto", productDto);
        return "product-detail";
    }


    


    


}
