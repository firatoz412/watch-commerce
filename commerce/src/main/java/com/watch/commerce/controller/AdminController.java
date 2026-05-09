package com.watch.commerce.controller;
import java.math.BigDecimal;
import java.security.Principal;
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
import com.watch.commerce.dto.UserDto;
import com.watch.commerce.request.AddProductRequest;
import com.watch.commerce.request.UpdateProductRequest;
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


    //admin dashoard sayfasını açar
    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model){
        model.addAttribute("totalProducts", productService.getAllProducts().size());
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalOrders", 0);   // sipariş servisin hazır olunca
        model.addAttribute("totalRevenue", BigDecimal.ZERO); // gelir servisin hazır olunca
        model.addAttribute("adminName", "Admin");
        return "Admin-dashboard";
    }

    //admin users listesini görür
    @GetMapping("/admin/users")
    public String users(Model model,Principal principal){

        List<UserDto> users = userService.getAllUsers();
        model.addAttribute("users", users);

        if (principal != null) {
            model.addAttribute("adminName", principal.getName());
        }

        return "users";
    }

     //user
    @PostMapping("/admin/users/delete/{userId}")
    public String deleteUser(Model model,Principal principal,@PathVariable Long userId){

        if(principal == null){
            return "redirect:/login";
        }
        userService.deleteUser(userId);
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
        model.addAttribute("categories", categoryService.getAllCategories()); // bunu ekle
        model.addAttribute("adminName", "Admin");
        
        return "productForm";
    }

    @PostMapping("/admin/products/update/{productId}")//ürünü güncelle
    public String updateProduct(@ModelAttribute UpdateProductRequest product,@PathVariable Long productId){
        productService.updateProduct(product, productId);//product = request
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/delete/{productId}")//ürünü sil
    public String deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);
        return "redirect:/admin/products";
    }


    
}
