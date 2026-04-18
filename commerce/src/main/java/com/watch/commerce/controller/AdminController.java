package com.watch.commerce.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.watch.commerce.model.Product;
import com.watch.commerce.model.User;
import com.watch.commerce.repository.ProductRepository;
import com.watch.commerce.service.product.ProductService;
import com.watch.commerce.service.user.UserService;

@Controller
public class AdminController{

    private final ProductService productService;
    private final UserService userService;

    public AdminController(ProductService productService, ProductRepository productRepository,UserService userService){
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/admin/products")
    public String adminPanel(Model model,@RequestParam(required=false) String brand){

        List<Product> products;

        if (brand != null && !brand.isEmpty()) {
            products = productService.getProductsByBrand(brand);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("activeBrand", brand); // nullable
        model.addAttribute("adminName", "Admin"); 
        return "admin-products";
    }


    @GetMapping("/admin/dashboard")
    public String adminDashboard(){
        return "dashboard";
    }

    @GetMapping("/admin/users")
    public String users(Model model){
        List<User> users = userService.getAllUser();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/admin/list")//ürünleri listele
    public String listProducts(){
        return "products";
    }

    @GetMapping("/admin/products/new")
    public String addForm(){//yeni ürün ekleme formu sayfasını açar
        return "newProduct";
    }


    @PostMapping("/admin/products/add")//yeni ürün ekle
    public String addNewProduct(@RequestParam Product product, Model model){
        productService.addProduct(product);
        return "redirect:/admin/products";
    }


    @GetMapping("/admin/products/edit/{productId}")//ürür formunu getir
    public String editProduct(Model model,@PathVariable Long productId){

        Product product = productService.getProductById(productId);
        model.addAttribute("productId", product);
        
        return "admin/productForm";
    }

    @PostMapping("/admin/products/update/{productId}")//ürünğ güncelle
    public String updateProduct(@ModelAttribute Product product,@PathVariable Long productId){
        productService.updateProduct(product, productId);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/delete/{productId}")//ürünü sil
    public String deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return "redirect:/admin/products";
    }


}