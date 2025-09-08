package com.ecommernce.exceptions;

public class APIExcpetion extends RuntimeException{

	    private static final long serialVersionUID=1L;
	    
	    
	     
	     public APIExcpetion() {
	    	 
	     }
	     public APIExcpetion(String message) {
	    	 super(message);
	     }
}
