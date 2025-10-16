package com.ecommernce.payload;

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
public class OrderRequestDTO {
	
	    private Long addressId;
	    private String paymentMethod;
	    private String pgName;
	    private String pgPaymentId;
	    private String pgStatus;
	    private String pgResponseMessage;

}
