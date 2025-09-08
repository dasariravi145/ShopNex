package com.ecommernce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommernce.exceptions.APIExcpetion;
import com.ecommernce.exceptions.ResourceNotFoundException;
import com.ecommernce.model.Category;
import com.ecommernce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	     @Autowired
	     private CategoryRepository categoryRepository;
         @Override
		 public List<Category> getAllCategories() {
			// TODO Auto-generated method stub
        	List<Category> categories= categoryRepository.findAll();
        	  if(categories.isEmpty())
        	 throw new APIExcpetion("Categories not created still Empty");
			 return categories;  
		 }

		 @Override
		 public void createCategory(Category category) {
			 Category c= categoryRepository.findByCategoryName(category.getCategoryName());
			 if(c!=null)
				 throw new APIExcpetion("Category With this name"+category.getCategoryName()+"already Existing");
			 categoryRepository.save(category);
			
		 }

		 @Override
		 public String deleteCategory(Long categoryId) {
			// TODO Auto-generated method stub
			 Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category",categoryId));
			 categoryRepository.delete(category);
			return "Category With categories:::" +categoryId+"::Deleted";
		 }

		 @Override
		 public Category updateCategory(Category category, Long categoryId) {
			     Category existing=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category",categoryId));
		    	 existing.setCategoryName(category.getCategoryName());
		         return categoryRepository.save(existing);
		
		 }

		
	     
	     
	     
}
