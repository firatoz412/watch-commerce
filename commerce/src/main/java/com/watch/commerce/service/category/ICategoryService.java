package com.watch.commerce.service.category;

import java.util.List;

import com.watch.commerce.model.Category;

public interface  ICategoryService {

    Category getCategory(Long categoryId);

    List<Category> getAllCategories();

}
