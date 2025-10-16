package com.ecommernce.service;

import java.util.List;

import com.ecommernce.payload.CartDTO;

public interface CartService {
	
	   CartDTO addProductsToCart(Long productId,Integer quantity);
	   
	   List<CartDTO> getAllCartItems();
	   
	   CartDTO getCartsUserById(String email,Long id);
	   
	   String deleteProductFromCart(Long cartId,Long productId);
	   
	   CartDTO updateProductQuantityInCart(Long productId,Integer quantity);
	   
	   void updateProductInCarts(Long cartId,Long productId);
	   

}
