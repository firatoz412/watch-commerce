package com.watch.commerce.service.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.model.Product;
import com.watch.commerce.request.AddProductRequest;

public interface IProductService {


    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto addProduct(AddProductRequest request,MultipartFile file);

    Product updateProduct(Product product, Long id);

    void deleteProduct(Long productId);

    List<ProductDto> searchProducts(String brand);

    ProductDto convertToDto(Product product);

    List<ProductDto> convertToDto(List<Product> products);



    

    
}
