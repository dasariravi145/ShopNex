package com.ecommernce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommernce.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

	@Query("SELECT c FROM Cart c WHERE c.user.email=?1")
	Cart findCartByEmail(String loggedInEmail);
	
	@Query("SELECT c FROM Cart c WHERE c.user.email=?1 AND c.cartId=?2")
	Cart findByEmailAndCartId(String email,Long id);
	
	@Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id=?1")
	List<Cart> findCartsByProductId(Long productId);

}
