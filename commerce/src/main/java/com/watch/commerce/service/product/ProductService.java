package com.watch.commerce.service.product;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Product;
import com.watch.commerce.repository.ProductRepository;



@Service
public class ProductService implements IProductService {
    
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository; 
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();   
    }
 
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }

    @Override
    public Product addProduct(Product product) {
        if(product == null){
            throw new IllegalArgumentException("product name cannot be empty.");
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product, Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));

        existingProduct.setName(product.getName());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setImage(product.getImage());

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));
        productRepository.delete(product);

    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if(keyword == null || keyword.trim().isEmpty()){
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(keyword, keyword);
    }

   


    



    


}
