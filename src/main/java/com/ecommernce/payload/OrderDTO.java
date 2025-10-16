package com.ecommernce.payload;

import java.time.LocalDate;
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
public class OrderDTO {

	private Long orderId;
    private String email;
    private List<OrderItemDTO> orderItem;
    private LocalDate orderDate;
    
    private PaymentDTO payment;
    
    private Double totalAmount;
    
    private String orderStatus;
    private Long addressId;
}
