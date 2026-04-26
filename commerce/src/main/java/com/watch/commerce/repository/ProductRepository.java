package com.watch.commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.watch.commerce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {


    List<Product> findAll();
    
    List<Product> findByBrand(String brand);

    Optional<Product> findById(Long productId);

    List<Product> findByNameContaining(String productName);

    
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.brand) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> search(@Param("keyword") String keyword);

}