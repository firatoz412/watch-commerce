package com.watch.commerce.service.product;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.dto.ProductDto;
import com.watch.commerce.dto.ProductImageDto;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Category;
import com.watch.commerce.model.Product;
import com.watch.commerce.model.ProductImage;
import com.watch.commerce.repository.CategoryRepository;
import com.watch.commerce.repository.ProductRepository;
import com.watch.commerce.request.AddProductRequest;



@Service
public class ProductService implements IProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository,CategoryRepository categoryRepository){
        this.productRepository = productRepository; 
        this.categoryRepository = categoryRepository;
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
                System.out.println(e.getMessage());
            }
        }
        Product savedProduct = productRepository.save(product);
        return convertToDto(savedProduct);
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


        // Resimleri Entity listesinden DTO listesine çeviriyoruz
        if (product.getImage() != null) {
            List<ProductImageDto> imageDtos = product.getImage().stream()
                .map(img -> {
                    ProductImageDto imgDto = new ProductImageDto();
                    imgDto.setId(img.getId());
                    imgDto.setImageUrl(img.getImageUrl());
                    return imgDto;
                }).toList();
            dto.setImages(imageDtos);
        }
        
        return dto;
    }


    @Override
    public List<ProductDto> convertToDto(List<Product> products) {
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    



    


}
