package com.ecommernce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="CART_ITEM")
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartItem {
	
	    @Id
	    @GeneratedValue(strategy=GenerationType.IDENTITY)
	    private Long cartItemId;
	    @ManyToOne
	    @JoinColumn(name="CART_ID")
	    private Cart cart;
	    @ManyToOne
	    @JoinColumn(name="PRODUCT_ID")
	    private Product product;
	    private Integer quantity;
	    private Double discount;
	    private Double productPrice;

}
