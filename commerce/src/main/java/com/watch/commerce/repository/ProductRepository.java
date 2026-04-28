package com.watch.commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findAll();
    
    List<Product> findByBrand(String brand);

    Optional<Product> findById(Long productId);

    List<Product> findByNameContainingIgnoreCaseOrBrandContainingIgnoreCase(String name, String brand);

}