package com.watch.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.ProductImage;

public interface ImageRepository extends JpaRepository<ProductImage,Long>{
    
}
