package com.ecommernce.payload;

import java.util.List;

public class CategoryResponse {

	       private List<CategoryDTO> content;

		   public CategoryResponse() {
			super();
			// TODO Auto-generated constructor stub
		   }

		   public CategoryResponse(List<CategoryDTO> content) {
			super();
			this.content = content;
		   }

		   public List<CategoryDTO> getContent() {
			   return content;
		   }

		   public void setContent(List<CategoryDTO> content) {
			   this.content = content;
		   }
	       
	       
}
