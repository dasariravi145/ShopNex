package com.ecommernce.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommernce.exceptions.APIExcpetion;
import com.ecommernce.exceptions.ResourceNotFoundException;
import com.ecommernce.model.Category;
import com.ecommernce.payload.CategoryDTO;
import com.ecommernce.payload.CategoryResponse;
import com.ecommernce.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService{

	     @Autowired
	     private CategoryRepository categoryRepository;
	     @Autowired
	     private ModelMapper modelMapper;
         @Override
		 public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        	 
        	    Sort sortByAndOder=sortOrder.equalsIgnoreCase("ASC")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        	    
        	    Pageable pageDetails=PageRequest.of(pageNumber, pageSize,sortByAndOder);
        	    Page<Category> categoryPage=categoryRepository.findAll(pageDetails);
        	    List<Category> categories=categoryPage.getContent();
		
        	  if(categories.isEmpty())
        	 throw new APIExcpetion("Categories not created still Empty");
        	  List<CategoryDTO> respose=categories.stream()
        			  .map(category->modelMapper.map(category, CategoryDTO.class)).toList();
        	  CategoryResponse categoryResponse=new CategoryResponse();
        	  categoryResponse.setContent(respose);
        	  categoryResponse.setPageNumber(categoryPage.getNumber());
        	  categoryResponse.setPageSize(categoryPage.getSize());
        	  categoryResponse.setTotalElement(categoryPage.getTotalElements());
        	  categoryResponse.setTotalpages(categoryPage.getTotalPages());
        	  categoryResponse.setLastPage(categoryPage.isLast());
			 return categoryResponse;  
		 }

		 @Override
		 public CategoryDTO createCategory(CategoryDTO categoryDTO) {
			 Category category=modelMapper.map(categoryDTO, Category.class);
			 
			 Category categoryFromDB=categoryRepository.findByCategoryName(category.getCategoryName());
			 if(categoryFromDB!=null) 
				   throw new APIExcpetion("Category with the name"+category.getCategoryName()+"already exists !!!");
				   
			       Category savedCategory=categoryRepository.save(category);
			 
			     return modelMapper.map(savedCategory, CategoryDTO.class);
		 }

		 @Override
		 public CategoryDTO deleteCategory(Long categoryId) {
			// TODO Auto-generated method stub
			 Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category",categoryId));
			 categoryRepository.delete(category);
			 CategoryDTO categoryDTO=modelMapper.map(category, CategoryDTO.class);
			return categoryDTO;
		 }

		 @Override
		 public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
			     Category existing=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","category",categoryId));
			     Category updateCategory=modelMapper.map(existing, Category.class);
			     updateCategory.setCategoryId(categoryId);
		          categoryRepository.save(updateCategory);
		         return modelMapper.map(existing, CategoryDTO.class);
		
		 }

		
	     
	     
	     
}
