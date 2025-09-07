package com.ecommernce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ecommernce.model.Category;
import com.ecommernce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	     @Autowired
	     private CategoryRepository categoryRepository;
         @Override
		 public List<Category> getAllCategories() {
			// TODO Auto-generated method stub
			return categoryRepository.findAll();
		 }

		 @Override
		 public void createCategory(Category category) {
			 categoryRepository.save(category);
		 }

		 @Override
		 public String deleteCategory(Long categoryId) {
			// TODO Auto-generated method stub
			 Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Category Not Found"));
			 categoryRepository.delete(category);
			return "Category With categories:::" +categoryId+"::Deleted";
		 }

		 @Override
		 public Category updateCategory(Category category, Long categoryId) {
			     Category existing=categoryRepository.findById(categoryId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource Not Found"));
		    	 existing.setCategoryName(category.getCategoryName());
		         return categoryRepository.save(existing);
		
		 }

		
	     
	     
	     
}
