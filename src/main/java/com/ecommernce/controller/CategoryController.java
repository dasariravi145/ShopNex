package com.ecommernce.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ecommernce.model.Category;
import com.ecommernce.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	          @Autowired
	          private CategoryService categoryService;
	          
	          @GetMapping("/getAllCategories")
	          public ResponseEntity<List<Category>> getAllCategories(){
	        	  return new ResponseEntity<>(categoryService.getAllCategories(),HttpStatus.OK);
	          }
	          @PostMapping("/createCategory")
	          public ResponseEntity<String> createCategory(@Valid @RequestBody Category category) { 
	        	   categoryService.createCategory(category);
	        	   return new ResponseEntity<String>("Added Category Sucessfuly",HttpStatus.CREATED);
	          }
	          
	          @DeleteMapping("/deleted/{categoryId}")
	          public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
	        	  try {
	        	  String status= categoryService.deleteCategory(categoryId);
	        	  return new ResponseEntity<>(status,HttpStatus.OK);
	        	  }catch(ResponseStatusException ex) {
	        		  return new  ResponseEntity<>(ex.getReason(),ex.getStatusCode());
	        	  }
	          }
	          
	          @PutMapping("/updateCategory/{categoryId}")
	          public ResponseEntity<String> updateCategory(@RequestBody Category category,@PathVariable Long categoryId) {
	        	  try { 
	        	     categoryService.updateCategory(category, categoryId);
	        	     return new ResponseEntity<String>("Updated Sucessfuly"+categoryId,HttpStatus.OK);
	        	  }catch(ResponseStatusException ex) {
	        		  return new  ResponseEntity<>(ex.getReason(),ex.getStatusCode());
	        	  }
	          }
}

	          