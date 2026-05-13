package com.watch.commerce.service.product;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.dto.ProductImageDto;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Category;
import com.watch.commerce.model.Product;
import com.watch.commerce.repository.CategoryRepository;
import com.watch.commerce.repository.ImageRepository;
import com.watch.commerce.repository.ProductRepository;
import com.watch.commerce.request.AddProductRequest;
import com.watch.commerce.request.UpdateProductRequest;
import com.watch.commerce.service.image.ImageService;



@Service
public class ProductService implements IProductService {
    
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageService imageService;

    public ProductService(ProductRepository productRepository,
        CategoryRepository categoryRepository,
        ImageService imageService,
        ImageRepository imageRepository){
        this.productRepository = productRepository; 
        this.categoryRepository = categoryRepository;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @Override
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::convertToDto).toList();   
    }
 
    @Override
    public ProductDto getProductById(Long id) {
        Product product =  productRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));
        return convertToDto(product);
    }

    @Transactional
    @Override
    public ProductDto addProduct(AddProductRequest request,MultipartFile file){

        if(request == null){
            throw new IllegalArgumentException("addProductReqest verileri boş olamaz.");
        }
       
        Product product = new Product();
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setDescription(request.getDescription());
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> {throw new ResourceNotFoundException("kategori bulunamadı");});
        product.setCategory(category);
        
        Product savedProduct = productRepository.save(product);
        if (!file.isEmpty()) {
           imageService.saveImage(file, savedProduct);
        }
        return convertToDto(savedProduct);
    }

    

    @Override
    @Transactional
    public ProductDto updateProduct(UpdateProductRequest request,MultipartFile file, Long productId) {
            Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product not found"));

                existingProduct.setName(request.getProductName());
                existingProduct.setPrice(request.getPrice());
                existingProduct.setBrand(request.getBrand());
                existingProduct.setStock(request.getStock());

                if (request.getCategory() != null) {
                    existingProduct.setCategory(request.getCategory());
                }else{
                    Category category = new Category();
                    category.setName("watch");
                }

                if (file != null && !file.isEmpty()) {
                    if (existingProduct.getImage() != null) {
                        imageRepository.delete(existingProduct.getImage());
                        imageRepository.flush();
                        existingProduct.setImage(null);
                        productRepository.saveAndFlush(existingProduct);
                    }
                    imageService.saveImage(file, existingProduct);
                }
                Product updatedProduct = productRepository.save(existingProduct);
                return convertToDto(updatedProduct);

    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ResourceNotFoundException("product not found"));
        productRepository.delete(product);

    }

    @Override
    public List<ProductDto> searchProducts(String keyword) {
        List<Product> products;
        if(keyword == null || keyword.trim().isEmpty()){
            products = productRepository.findAll();
            return products.stream().map(this::convertToDto).toList();
        }
        products = productRepository.findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(keyword, keyword);
        return products.stream().map(this:: convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setStock(product.getStock());
        dto.setDescription(product.getDescription());

        // Resimleri Entity listesinden DTO listesine çeviriyoruz
        if (product.getImage() != null) {
           ProductImageDto imageDto = new ProductImageDto();
            imageDto.setId(product.getImage().getId());
            imageDto.setImageUrl(product.getImage().getImageUrl());
            dto.setImage(imageDto);
        }
        
        return dto;
    }


    @Override
    public List<ProductDto> convertToDto(List<Product> products) {
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    



    


}
