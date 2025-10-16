package com.ecommernce.exceptions;

public class ResourseNotFoundException extends RuntimeException{

	     String resourceName;
	     String field;
	     String fieldName;
	     Long fieldId;
		 
		 public ResourseNotFoundException() {
			super();
			// TODO Auto-generated constructor stub
		 }

		 public ResourseNotFoundException(String resourceName, String field, String fieldName) {
			super(String.format("%s not found with %s:%s", resourceName,field,fieldName));
			this.resourceName = resourceName;
			this.field = field;
			this.fieldName = fieldName;
		 }

		 public ResourseNotFoundException(String resourceName, String field, Long fieldId) {
			 super(String.format("%s not found with %s:%s", resourceName,field,fieldId));
			this.resourceName = resourceName;
			this.field = field;
			this.fieldId = fieldId;
		 }
		 
	     
	     
}
