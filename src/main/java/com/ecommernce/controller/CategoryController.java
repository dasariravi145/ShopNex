package com.ecommernce.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommernce.payload.CategoryDTO;
import com.ecommernce.payload.CategoryResponse;
import com.ecommernce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	          @Autowired
	          private CategoryService categoryService;
	          
	          @GetMapping("/getAllCategories")
	          public ResponseEntity<CategoryResponse> getAllCategories(){
	        	  return new ResponseEntity<>(categoryService.getAllCategories(),HttpStatus.OK);
	          }
	          @PostMapping("/createCategory")
	          public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) { 
	        	   CategoryDTO saveCategoryDTO= categoryService.createCategory(categoryDTO);
	        	   return new ResponseEntity<CategoryDTO>(saveCategoryDTO,HttpStatus.CREATED);
	          }
	          
	          @DeleteMapping("/deleted/{categoryId}")
	          public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
	        	  CategoryDTO status= categoryService.deleteCategory(categoryId);
	        	  return new ResponseEntity<>(status,HttpStatus.OK);
	          }
	          
	          @PutMapping("/updateCategory/{categoryId}")
	          public ResponseEntity<CategoryDTO> updateCategory(@RequestBody CategoryDTO categoryDTO,@PathVariable Long categoryId) {
	        	      CategoryDTO updateCategory=  categoryService.updateCategory(categoryDTO, categoryId);
	        	     return new ResponseEntity<CategoryDTO>(updateCategory,HttpStatus.OK);
	        	  
	          }
}

	          