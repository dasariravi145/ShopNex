package com.ecommernce.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ecommernce.payload.ProductDTO;
import com.ecommernce.payload.ProductResponse;

public interface ProductService {
	
	     public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	     
	     public ProductDTO addProducts(ProductDTO productDTO,Long categoryId);
	     
	     public ProductDTO deleteProduct(Long productId);
	     
	     public ProductDTO updateProduct(ProductDTO productDTO,Long productId);
	     
	     public ProductDTO updateImage(Long productId, MultipartFile file)throws IOException;
	     
	     public ProductResponse getAllProductByProductId(Long productId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
	     
	     public ProductResponse getAllProductsByKeyword(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder,String keyword);

}
