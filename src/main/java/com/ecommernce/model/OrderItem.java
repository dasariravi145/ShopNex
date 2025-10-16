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
@Table(name="ORDER_ITEMS")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class OrderItem {
	
	     @Id
	     @GeneratedValue(strategy=GenerationType.IDENTITY)
	     private Long orderItemId;
	     
	     @ManyToOne
	     @JoinColumn(name="PRODUCT_ID")
	     private Product product;
	     
	     @ManyToOne
	     @JoinColumn(name="ORDER_ID")
	     private Order order;
	     
	     private Integer quantity;
	     
	     private double discount;
	     
	     private double orderedProductPrice;

}
