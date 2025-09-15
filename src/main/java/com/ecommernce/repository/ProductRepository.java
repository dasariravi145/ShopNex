package com.ecommernce.repository;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommernce.model.Category;
import com.ecommernce.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	Page<Product> findByProductNameLikeIgnoreCase(String keyword,Pageable pageDetails);

	Page<Product> findByCategoryOrderByPriceAsc(Category category,Pageable pagedetails);

}
