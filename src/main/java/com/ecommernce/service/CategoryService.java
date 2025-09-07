package com.ecommernce.service;

import java.util.List;

import com.ecommernce.model.Category;

public interface CategoryService {

			 public List<Category> getAllCategories();
			 
			 public void createCategory(Category category);
			 
			 public String deleteCategory(Long categoryId);
			 
			 public Category updateCategory(Category category,Long categoryId);
}
