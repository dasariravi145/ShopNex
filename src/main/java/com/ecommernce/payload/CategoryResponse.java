package com.ecommernce.payload;

import java.util.List;

public class CategoryResponse {

	       private List<CategoryDTO> content;
	       private Integer pageNumber;
	       private Integer pageSize;
	       private Long totalElement;
	       private Integer totalpages;
	       private boolean lastPage;

		   public CategoryResponse() {
			super();
			// TODO Auto-generated constructor stub
		   }

		   public CategoryResponse(List<CategoryDTO> content, Integer pageNumber, Integer pageSize, Long totalElement,
				Integer totalpages, boolean lastPage) {
			super();
			this.content = content;
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
			this.totalElement = totalElement;
			this.totalpages = totalpages;
			this.lastPage = lastPage;
		}
		   public List<CategoryDTO> getContent() {
			   return content;
		   }

		   public void setContent(List<CategoryDTO> content) {
			   this.content = content;
		   }

		   public Integer getPageNumber() {
			   return pageNumber;
		   }

		   public void setPageNumber(Integer pageNumber) {
			   this.pageNumber = pageNumber;
		   }

		   public Integer getPageSize() {
			   return pageSize;
		   }

		   public void setPageSize(Integer pageSize) {
			   this.pageSize = pageSize;
		   }

		   public Long getTotalElement() {
			   return totalElement;
		   }

		   public void setTotalElement(Long totalElement) {
			   this.totalElement = totalElement;
		   }

		   public Integer getTotalpages() {
			   return totalpages;
		   }

		   public void setTotalpages(Integer totalpages) {
			   this.totalpages = totalpages;
		   }

		   public boolean isLastPage() {
			   return lastPage;
		   }

		   public void setLastPage(boolean lastPage) {
			   this.lastPage = lastPage;
		   }
	       
	       
}
