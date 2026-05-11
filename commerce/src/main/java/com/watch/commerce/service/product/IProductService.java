package com.watch.commerce.service.product;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.model.Product;
import com.watch.commerce.request.AddProductRequest;
import com.watch.commerce.request.UpdateProductRequest;

public interface IProductService {


    List<ProductDto> getAllProducts();

    ProductDto getProductById(Long id);

    ProductDto addProduct(AddProductRequest request,MultipartFile file);

    ProductDto updateProduct(UpdateProductRequest request,MultipartFile file, Long id);

    void deleteProduct(Long productId);

    List<ProductDto> searchProducts(String brand);

    ProductDto convertToDto(Product product);

    List<ProductDto> convertToDto(List<Product> products);



    

    
}
