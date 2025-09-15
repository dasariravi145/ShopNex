package com.ecommernce.payload;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

	       private List<ProductDTO> content;
	       private Integer pageNumber;
	       private Integer pageSize;
	       private Long totalElemts;
	       private Integer totalPages;
	       private Boolean lastPage;
}
