package com.watch.commerce.controller;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.model.Product;
import com.watch.commerce.model.User;
import com.watch.commerce.request.AddProductRequest;
import com.watch.commerce.service.category.CategoryService;
import com.watch.commerce.service.product.ProductService;
import com.watch.commerce.service.user.UserService;

@Controller
public class AdminController{

    private final ProductService productService;
    private final UserService userService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService,
                           UserService userService,
                           CategoryService categoryService    
                        ){
        this.productService = productService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/products")//admin sayfasında ürünleri görür
    public String adminPanel(Model model,@RequestParam(required=false) String keyword){

        List<ProductDto> products = productService.searchProducts(keyword);

        model.addAttribute("products", products);
        model.addAttribute("activeBrand", keyword); // nullable
        model.addAttribute("adminName", "Admin"); 
        return "admin-products";
    }


    @GetMapping("/admin/dashboard")
    public String adminDashboard(){
        return "dashboard";
    }

    @GetMapping("/admin/users")
    public String users(Model model){
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/admin/list")//ürünleri listele
    public String listProducts(){
        return "products";
    }

    @GetMapping("/admin/products/new")//yeni ürün ekleme formu sayfasını açar
    public String addForm(Model model){
        model.addAttribute("productRequest",new AddProductRequest()); 
        model.addAttribute("categories",categoryService.getAllCategories());
        return "productForm";
    }


   @PostMapping("/admin/products/add")
   public String addNewProduct(@ModelAttribute AddProductRequest request, 
                                @RequestParam("productImage") MultipartFile file) {

        try {
            productService.addProduct(request, file);
            return "redirect:/admin/products";
        } catch (Exception e) {
            return "productForm"; 
        }
    } 

    @GetMapping("/admin/products/edit/{productId}")//ürür formunu getir
    public String editProduct(Model model,@PathVariable Long productId){

        ProductDto product = productService.getProductById(productId);
        model.addAttribute("product", product);
        
        return "productForm";
    }

    @PostMapping("/admin/products/update/{productId}")//ürünü güncelle
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
