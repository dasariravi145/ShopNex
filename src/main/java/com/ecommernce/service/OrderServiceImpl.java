package com.ecommernce.service;

import java.lang.module.ResolutionException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommernce.exceptions.APIExcpetion;
import com.ecommernce.exceptions.ResourseNotFoundException;
import com.ecommernce.model.Address;
import com.ecommernce.model.Cart;
import com.ecommernce.model.CartItem;
import com.ecommernce.model.Order;
import com.ecommernce.model.OrderItem;
import com.ecommernce.model.Product;
import com.ecommernce.payload.OrderDTO;
import com.ecommernce.payload.OrderItemDTO;
import com.ecommernce.repository.AddressRepository;
import com.ecommernce.repository.CartRepository;
import com.ecommernce.repository.OrderItemRepository;
import com.ecommernce.repository.OrderRepository;
import com.ecommernce.repository.ProductRepository;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private AddressRepository addressRepository;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CartService cartService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId,
			String pgStatus, String pgResponseMessage) {

		Cart cart = cartRepository.findCartByEmail(emailId);
		if (cart == null) {
			throw new ResourseNotFoundException("Cart", "emailId", emailId);
		}
		Address address = addressRepository.findById(addressId)
				.orElseThrow(() -> new ResourseNotFoundException("Address", "addressId", addressId));

		Order order = new Order();
		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());
		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted!");
		order.setAddress(address);

		Order savedOrder = orderRepository.save(order);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.isEmpty()) {

			throw new APIExcpetion("Cart Is Empty");
		}
		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem cartItem : cartItems) {

			OrderItem orderItem = new OrderItem();
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);
			orderItems.add(orderItem);
		}
		orderItems = orderItemRepository.saveAll(orderItems);
		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();
			Product product = new Product();
			// Reduce Stock Quantity
			product.setQuantity(product.getQuantity() - quantity);
			// Save product back to the database
			productRepository.save(product);
			cartService.deleteProductFromCart(cart.getCartId(), item.getProduct().getProductId());
		});
		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		orderItems.forEach(item -> orderDTO.getOrderItem().add(modelMapper.map(item, OrderItemDTO.class)));
		return orderDTO;
	}

}
