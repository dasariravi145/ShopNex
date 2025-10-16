package com.ecommernce.payload;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartDTO {
	
	    private Long cartId;
	    private Double totalPrice=0.00;
	    private List<ProductDTO> products=new ArrayList<>();

}
