package com.ecommernce.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

	   private Long productId;
	   private String productName;
	   private String description;
	   private Integer price;
	   private Double discount;
	   private Double specialPrice;
	   private String image;
}
