package com.ecommernce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommernce.payload.OrderDTO;
import com.ecommernce.payload.OrderRequestDTO;
import com.ecommernce.service.OrderService;
import com.ecommernce.util.AuthUtil;

@RestController
@RequestMapping("/api")
public class OrderController {
	
	    @Autowired
	    private OrderService orderService;
	    
	    @Autowired
	    private AuthUtil authUtil;
	    
	    @PostMapping("/order/users/payments/{paymentMethod}")
	    public ResponseEntity<OrderDTO> orderProduct(@PathVariable String paymentMethod,@RequestBody OrderRequestDTO orderRequestDTO){
	    	
	    	  String emailId=authUtil.loggedInEmail();
	    	  OrderDTO orderDTO=orderService.placeOrder(emailId,
	    			  orderRequestDTO.getAddressId(),
	    			  paymentMethod,
	    			  orderRequestDTO.getPgName(),
	    			  orderRequestDTO.getPgPaymentId(), 
	    			  orderRequestDTO.getPgStatus(),
	    			  orderRequestDTO.getPgResponseMessage());
	    	  return new ResponseEntity<OrderDTO>(orderDTO,HttpStatus.CREATED);
	    }

}
