package com.watcher.service;

import java.util.List;

import com.watcher.custom_exception.ApiException;
import com.watcher.dto.OrderDto;

public interface OrderService {
	
	OrderDto placeOrder(String emailId, int cartId, String paymentMethod);
	
	OrderDto getOrder(String emailId, int orderId);
	
	List<OrderDto> getOrdersByUser(String emailId);
	
	List<OrderDto> getAllOrders();
	
	OrderDto updateOrder(String emailId, int orderId, String orderStatus);
	
	long getNumberOfOrders();
}
