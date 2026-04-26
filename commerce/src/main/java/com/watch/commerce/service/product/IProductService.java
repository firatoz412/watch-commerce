package com.watch.commerce.service.product;

import java.util.List;

import com.watch.commerce.model.Product;

public interface IProductService {


    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product addProduct(Product product);

    Product updateProduct(Product product, Long id);

    void deleteProduct(Long productId);

    List<Product> findByNameContaining(String productName);

    List<Product> findByBrand(String brand);

    List<Product> getProducts(String brand);

    

    
}
