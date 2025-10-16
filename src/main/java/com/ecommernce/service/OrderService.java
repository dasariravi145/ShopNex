package com.ecommernce.service;

import com.ecommernce.payload.OrderDTO;

import jakarta.transaction.Transactional;

public interface OrderService {
	
	   @Transactional
	   OrderDTO placeOrder(String emailId,Long addressId,String paymentMethod,String pgName,String pgPaymentId,String pgStatus,String pgResponseMessage);

}
