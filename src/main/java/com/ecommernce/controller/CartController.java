package com.ecommernce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommernce.model.Cart;
import com.ecommernce.payload.CartDTO;
import com.ecommernce.repository.CartRepository;
import com.ecommernce.service.CartService;
import com.ecommernce.util.AuthUtil;

@RestController
@RequestMapping("/api")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private AuthUtil authUtil;

	@Autowired
	private CartRepository cartRepository;

	@PostMapping("/cart/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProductsToCart(@PathVariable Long productId, @PathVariable Integer quantity) {

		CartDTO cartDTO = cartService.addProductsToCart(productId, quantity);
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
	}

	@GetMapping("/cart/products/getAllCartItems")
	public ResponseEntity<List<CartDTO>> getCarts() {

		List<CartDTO> cartDTOList = cartService.getAllCartItems();
		return new ResponseEntity<List<CartDTO>>(cartDTOList, HttpStatus.FOUND);
	}

	@GetMapping("/carts/users/cart")
	public ResponseEntity<CartDTO> getCartsByUserId() {

		String email = authUtil.loggedInEmail();
		Cart cart = cartRepository.findCartByEmail(email);

		Long id = cart.getCartId();

		CartDTO cartDTOList = cartService.getCartsUserById(email, id);
		return new ResponseEntity<CartDTO>(cartDTOList, HttpStatus.FOUND);
	}

	@PostMapping("/cart/products/{productId}/quantities/{operation}")
	public ResponseEntity<CartDTO> updateProduct(@PathVariable Long productId, @PathVariable String operation) {
		CartDTO cartDTO = cartService.updateProductQuantityInCart(productId,
				operation.equalsIgnoreCase("delete") ? -1 : 1);
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.OK);

	}

	@DeleteMapping("/carts/{cartId}/product/{productId}")
	public ResponseEntity<String> deleteProductFromCart(@PathVariable Long cartId, @PathVariable Long productId) {
		String status = cartService.deleteProductFromCart(cartId, productId);

		return new ResponseEntity<String>(status, HttpStatus.OK);

	}

}
