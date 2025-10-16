package com.ecommernce.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommernce.exceptions.APIExcpetion;
import com.ecommernce.exceptions.ResourseNotFoundException;
import com.ecommernce.model.Cart;
import com.ecommernce.model.CartItem;
import com.ecommernce.model.Product;
import com.ecommernce.payload.CartDTO;
import com.ecommernce.payload.ProductDTO;
import com.ecommernce.repository.CartItemRepository;
import com.ecommernce.repository.CartRepository;
import com.ecommernce.repository.ProductRepository;
import com.ecommernce.util.AuthUtil;

import jakarta.transaction.Transactional;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private AuthUtil authUtil;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CartDTO addProductsToCart(Long productId, Integer quantity) {
		
		Cart cart=createCart();
		
	    Product product=productRepository.findById(productId)
	    		.orElseThrow(()->new ResourseNotFoundException("Product","productId",productId));
	    
	    CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cart.getCartId(),productId);
	    
	    if(cartItem !=null) {
	    	throw new APIExcpetion("Product" +product.getProductName()+"Already Exists In The Cart");
	    }
	    
	    if(product.getQuantity()==0) {
	    	throw new APIExcpetion(product.getProductName()+"Is Not Available");
	    }
	    
	    if(product.getQuantity()<quantity)
	    {
	    	throw new APIExcpetion("Please, make an order od the:" 
	                  +product.getProductName()+"less than or equal to the quantity:" +product.getQuantity()+".");
	    }
	    
	    CartItem newCartItem=new CartItem();
	    
	    newCartItem.setProduct(product);
	    newCartItem.setCart(cart);
	    newCartItem.setQuantity(quantity);
	    newCartItem.setDiscount(product.getDiscount());
	    newCartItem.setProductPrice(product.getSpecialPrice());
	    
	    cartItemRepository.save(newCartItem);
	    
	    product.setQuantity(product.getQuantity());
	    
	    cart.setTotalPrice(cart.getTotalPrice()+(product.getSpecialPrice()*quantity));
	    
	    cartRepository.save(cart);
	    
	    CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
	    
	    List<CartItem> cartItems=cart.getCartItems();
	    
	    Stream<ProductDTO> productStream=cartItems.stream().map(item->{
	    	ProductDTO map=modelMapper.map(item.getProduct(), ProductDTO.class);
	    	map.setQuantity(item.getQuantity());
	    	return map;
	    });
	    
	    cartDTO.setProducts(productStream.toList());
	    	
		return cartDTO;
	}
	

	@Override
	public List<CartDTO> getAllCartItems() {
		List<Cart> carts=cartRepository.findAll();
		
		if(carts.size()==0) {
			  throw new APIExcpetion("No Cart Exists");
		}
		List<CartDTO> cartDTOs=carts.stream().map(cart->{
			  CartDTO cartDTO=modelMapper.map(cart, CartDTO.class); 
			  List<ProductDTO> products=cart.getCartItems().stream().map(cartItem->{
				   ProductDTO productDTO=modelMapper.map(cartItem.getProduct(), ProductDTO.class);
				   productDTO.setQuantity(cartItem.getQuantity());
				   return productDTO;
			  }).collect(Collectors.toList());
			  cartDTO.setProducts(products);
			  return cartDTO;
		}).collect(Collectors.toList());
		return cartDTOs;
	}

	@Override
	public CartDTO getCartsUserById(String email, Long id) {
		Cart cart=cartRepository.findByEmailAndCartId(email, id);
		
		if(cart==null) {
			throw new ResourseNotFoundException("Cart","cartId",id);
		}
		CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
		cart.getCartItems().forEach(p->p.getProduct().setQuantity(p.getQuantity()));
		List<ProductDTO> productDTOs=cart.getCartItems().stream()
				.map(p->modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
		cartDTO.setProducts(productDTOs);
		return cartDTO;
	}

	@Transactional
	@Override
	public String deleteProductFromCart(Long cartId, Long productId) {
		 Cart cart=cartRepository.findById(cartId)
				 .orElseThrow(()->new ResourseNotFoundException("Cart","cartId",cartId));
		 
		 CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
		 
		 if(cartItem==null) {
			 
			  throw new ResourseNotFoundException("Product","productId",productId);
		 }
		 cart.setTotalPrice(cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity()));
		 cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);
		return "Product" +cartItem.getProduct().getProductName()+"removed from the cart!!!";
	}
    @Transactional
	@Override
	public CartDTO updateProductQuantityInCart(Long productId, Integer quantity) {
		// TODO Auto-generated method stub
    	
    	String emailId=authUtil.loggedInEmail();
    	Cart userCart=cartRepository.findCartByEmail(emailId);
    	
    	Long cartId=userCart.getCartId();
    	 
    	Cart cart=cartRepository.findById(cartId)
    			.orElseThrow(()->new ResourseNotFoundException("Cart","cartId",cartId));
    	Product product=productRepository.findById(productId)
    			.orElseThrow(()->new ResourseNotFoundException("Product","productId",productId));
    	
    	if(product.getQuantity()==0) {
    		
    		  throw new APIExcpetion(product.getProductName()+"::is not available");
    	}
    	
    	if(product.getQuantity()<quantity) {
    		
    		throw new APIExcpetion("Please, make an order od the:" 
	                  +product.getProductName()+"less than or equal to the quantity:" +product.getQuantity()+".");
    	}
    	CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
    	
    	 if(cartItem==null) {
			 
			  throw new APIExcpetion("Product::" +product.getProductName()+"::not available in the cart!!!");
		 }
    	 
    	 //Calculate new Quantity
    	 int newQuantity=cartItem.getQuantity()+quantity;
    	 
    	 if(newQuantity<0) {
    		 throw new APIExcpetion("The resulting quantity be negative");
    	 }
    	 if(newQuantity==0) {
    		 deleteProductFromCart(cartId, productId);
    	 }else {
    		 
    		   cartItem.setProductPrice(product.getSpecialPrice());
    		   cartItem.setQuantity(cartItem.getQuantity()+quantity);
    		   cartItem.setDiscount(product.getDiscount());
    		   cart.setTotalPrice(cart.getTotalPrice()+(cartItem.getProductPrice()* quantity));
    		   cartRepository.save(cart);
    	 }
    	 
    	 CartItem updatedItem=cartItemRepository.save(cartItem);
    	 
    	 if(updatedItem.getQuantity()==0) {
    		 cartItemRepository.deleteById(updatedItem.getCartItemId());
    	 }
    	 
    	 CartDTO cartDTO=modelMapper.map(cart, CartDTO.class);
    	 
    	 List<CartItem> cartItems=cart.getCartItems();
    	 
    	 
    	 Stream<ProductDTO> productStream=cartItems.stream().map(item->{
    		   ProductDTO prd=modelMapper.map(item.getProduct(), ProductDTO.class); 
    		   prd.setQuantity(item.getQuantity());
    		   return prd;
    	 });
    	 
    	 cartDTO.setProducts(productStream.toList());
    	 
		return cartDTO;
	}

	@Override
	public void updateProductInCarts(Long cartId, Long productId) {
		// TODO Auto-generated method stub
		
		Cart cart=cartRepository.findById(cartId)
				.orElseThrow(()->new ResourseNotFoundException("Cart","cartId",cartId));
		Product product=productRepository.findById(productId)
				.orElseThrow(()->new ResourseNotFoundException("Cart","cartId",cartId));
		CartItem cartItem=cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
		if(cartItem==null) {
			
			throw new APIExcpetion("Product" +product.getProductName()+"Not avaible in the cart!");
		}
		double cartPrice=cart.getTotalPrice()-(cartItem.getProductPrice()*cartItem.getQuantity());
		cartItem.setProductPrice(product.getSpecialPrice());
		cart.setTotalPrice(cartPrice+(cartItem.getProductPrice()*cartItem.getQuantity()));
		cartItem=cartItemRepository.save(cartItem);
	}
	
	public Cart createCart() {
		   Cart userCart=cartRepository.findCartByEmail(authUtil.loggedInEmail());
		   
		   if(userCart!=null) {
			   return userCart;
		   }
		   Cart cart=new Cart();
		   cart.setTotalPrice(0.00);
		   cart.setUser(authUtil.loggedInUser());
		   return cartRepository.save(cart);
		   
	}

}
