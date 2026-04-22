package com.watch.commerce.service.category;

import java.util.List;

import org.springframework.stereotype.Service;

import com.watch.commerce.model.Category;
import com.watch.commerce.repository.CategoryRepository;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository =categoryRepository;
    }
    
    @Override
    public List<Category> getAllCategories(){
        return categoryRepository.findAll();
    }
}
