package com.ecommernce.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {
	
	   @Id
	   @GeneratedValue(strategy=GenerationType.IDENTITY)
	   private Long productId;
	   private String productName;
	   private String description;
	   private Integer price;
	   private Integer quantity;
	   private Double discount;
	   private Double specialPrice;
	   private String image;
	   
	   @ManyToOne
	   @JoinColumn(name="CATEGORY_ID")
	   private Category category;	
	   
	   @ManyToOne
	   @JoinColumn(name="SELLER_ID")
	   private User user;

}
