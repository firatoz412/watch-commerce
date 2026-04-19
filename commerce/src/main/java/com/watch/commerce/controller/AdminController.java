package com.watch.commerce.controller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.model.Product;
import com.watch.commerce.model.ProductImage;
import com.watch.commerce.model.User;
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
        model.addAttribute("product",new Product());
        model.addAttribute("categories",categoryService.getAllCategories());
        return "productForm";
    }


   @PostMapping("/admin/products/add")
   public String addNewProduct(@ModelAttribute Product product, 
                                @RequestParam("productImage") MultipartFile file) {

        String uploadDir = "C:/Users/firat/OneDrive/Masaüstü/commerce_final/commerce/src/main/resources/static/images/watches/";
        
        if (!file.isEmpty()) {
            try {
                //aynı isimde başka bir resim olmasın diye rastgele 36 haneli benzersiz metin üretiyoruz
                String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path savePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), savePath, StandardCopyOption.REPLACE_EXISTING);

                ProductImage productImage = new ProductImage();
                productImage.setImageUrl(fileName);
                productImage.setProduct(product);  

                if (product.getImage() == null) {
                    product.setImage(new ArrayList<>());
                }
                product.getImage().add(productImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    productService.addProduct(product);
    return "redirect:/admin/products";
} 

    @GetMapping("/admin/products/edit/{productId}")//ürür formunu getir
    public String editProduct(Model model,@PathVariable Long productId){

        Product product = productService.getProductById(productId);
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