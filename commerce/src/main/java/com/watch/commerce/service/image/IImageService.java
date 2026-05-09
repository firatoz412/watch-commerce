package com.watch.commerce.service.image;

import org.springframework.web.multipart.MultipartFile;

import com.watch.commerce.dto.ProductImageDto;
import com.watch.commerce.model.Product;

public interface IImageService {

    ProductImageDto saveImage(MultipartFile file,Product product);

    void deleteImage(Long imageId);
    
}
