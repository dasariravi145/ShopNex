package com.ecommernce.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommernce.config.APIConstants;
import com.ecommernce.payload.ProductDTO;
import com.ecommernce.payload.ProductResponse;
import com.ecommernce.service.ProductService;import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class ProductController {

	       @Autowired
	       private ProductService productService;
	       
	       @GetMapping("/public/getAllProducts")
	       public ResponseEntity<ProductResponse> getAllProducts(
	    		   @RequestParam(value="pageNumber",defaultValue=APIConstants.PAGE_NUMBER,required=false) Integer pageNumber,
	    		   @RequestParam(value="pageSize",defaultValue=APIConstants.PAGE_SIZE,required=false) Integer pageSize,
	    		   @RequestParam(value="sortBy",defaultValue=APIConstants.SOR_PRODUCTS_BY,required=false) String sortBy,
	    		   @RequestParam(value="sortOrder",defaultValue=APIConstants.SORT_DIR,required=false) String sortOrder
	    		   ) {
	    	   
	    	   ProductResponse productResponse=productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
	    	   return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
	       }
	       
	       @GetMapping("/public/getAllProdctsCategory/{categoryId}")
	       public ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId,@RequestParam(value="pageNumber",defaultValue=APIConstants.PAGE_NUMBER,required=false) Integer pageNumber,
	    		   @RequestParam(value="pageSize",defaultValue=APIConstants.PAGE_SIZE,required=false) Integer pageSize,
	    		   @RequestParam(value="sortBy",defaultValue=APIConstants.SOR_PRODUCTS_BY,required=false) String sortBy,
	    		   @RequestParam(value="sortOrder",defaultValue=APIConstants.SORT_DIR,required=false) String sortOrder){
	    	   
	    	       ProductResponse productResponse=productService.getAllProductByProductId(categoryId,pageNumber,pageSize,sortBy,sortOrder);
	    	       return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
	       }
	       @GetMapping("/public/getAllProductKeyWord/{keyword}")
	       public ResponseEntity<ProductResponse> getAllProductsByKeyword(@RequestParam(value="pageNumber",defaultValue=APIConstants.PAGE_NUMBER,required=false) Integer pageNumber,
	    		   @RequestParam(value="pageSize",defaultValue=APIConstants.PAGE_SIZE,required=false) Integer pageSize,
	    		   @RequestParam(value="sortBy",defaultValue=APIConstants.SOR_PRODUCTS_BY,required=false) String sortBy,
	    		   @RequestParam(value="sortOrder",defaultValue=APIConstants.SORT_DIR,required=false) String sortOrder,@PathVariable String keyword){
	    	   
	    	       ProductResponse productResponse=productService.getAllProductsByKeyword(pageNumber,pageSize,sortBy,sortOrder,keyword);
	    	       return new ResponseEntity<ProductResponse>(productResponse,HttpStatus.OK);
	       }
	       @PostMapping("/admin/addProduct/{categoryId}")
	       public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO,@PathVariable Long categoryId){
	    	   
	    	         ProductDTO savedProductDTO=productService.addProducts(productDTO, categoryId);
	    	         return new ResponseEntity<ProductDTO>(savedProductDTO,HttpStatus.CREATED);
	       }
	       @DeleteMapping("/admin/deletePrduct/{productId}")
	       public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
	    	   
	    	        ProductDTO productDTO=productService.deleteProduct(productId);
	    	        return new ResponseEntity<ProductDTO>(productDTO,HttpStatus.OK);
	       }
	       @PutMapping("/admin/updateProduct/{productId}")
	       public ResponseEntity<ProductDTO> updteProduct(@RequestBody ProductDTO productDTO,@PathVariable Long productId){
	    	   
	    	         ProductDTO updateProductDTO=productService.updateProduct(productDTO, productId);
	    	         return new ResponseEntity<ProductDTO>(updateProductDTO,HttpStatus.OK);
	       }
	       @PutMapping("/admin/{productId}/uploadImage")
	       public ResponseEntity<ProductDTO> uploadImage(@PathVariable Long productId,@RequestParam("image") MultipartFile image)throws IOException{
	    	
	    	         ProductDTO message=productService.updateImage(productId, image);
	    	         return new ResponseEntity<ProductDTO>(message,HttpStatus.ACCEPTED);
	       }
}
