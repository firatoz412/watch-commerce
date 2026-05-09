package com.watch.commerce.service.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.dto.ProductImageDto;
import com.watch.commerce.exception.ResourceNotFoundException;
import com.watch.commerce.model.Product;
import com.watch.commerce.model.ProductImage;
import com.watch.commerce.repository.ImageRepository;


@Service
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    //Resimlerin kaydedileceği klasör yolu (Örn: "src/main/resources/static/uploads")
    private final String uploadDir = "C:/ecommerce/uploads/";

    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    @Override
    public ProductImageDto saveImage(MultipartFile file,Product product) {
        try {
            //benzersiz dosya adı
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());//klasör yoksa oluşturur
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            
            ProductImage image = new ProductImage();
            image.setImageUrl(fileName);
            image.setProduct(product);

            ProductImage savedImage  = imageRepository.save(image);
            return convertToDto(savedImage);
            
        } catch (IOException e) {
            throw new RuntimeException("Dosya kaydedilirken hata oluştu: " + e.getMessage());
        }
    }

    @Override
    public void deleteImage(Long imageId) {
        ProductImage image = imageRepository.findById(imageId).orElseThrow(
            () -> new ResourceNotFoundException("silinecek resim bulunamadı")
        );
        imageRepository.delete(image);
    }

    public ProductImageDto convertToDto(ProductImage image){
        ProductImageDto dto = new ProductImageDto();
        dto.setId(image.getId());
        dto.setImageUrl(image.getImageUrl()); 
        
        if (image.getProduct() != null) {
            dto.setProductId(image.getProduct().getId());
        }

        return dto;

    }



    

  
    



    
}
