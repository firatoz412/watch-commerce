package com.watch.commerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.watch.commerce.model.Category;

public interface  CategoryRepository extends JpaRepository<Category, Long>{

    
}
