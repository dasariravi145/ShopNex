package com.ecommernce.service;



import com.ecommernce.model.Category;
import com.ecommernce.payload.CategoryDTO;
import com.ecommernce.payload.CategoryResponse;

public interface CategoryService {

			 public CategoryResponse getAllCategories();
			 
			 public CategoryDTO createCategory(CategoryDTO categoryDTO);
			 
			 public CategoryDTO deleteCategory(Long categoryId);
			 
			 public CategoryDTO updateCategory(CategoryDTO categoryDTO,Long categoryId);
}
