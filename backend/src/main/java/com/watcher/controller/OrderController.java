package com.watcher.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watcher.dto.OrderDto;
import com.watcher.service.OrderService;
import com.watcher.service.UserService;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")

public class OrderController {

	
	@Autowired
	public OrderService orderService;
	
	@PostMapping("/public/users/{emailId}/carts/{cartId}/payments/{paymentMethod}")
	public ResponseEntity<OrderDto> orderProducts(@PathVariable String emailId, @PathVariable int cartId, @PathVariable
			String paymentMethod) {
		System.out.println("start--------------------------");

		return ResponseEntity.ok(orderService.placeOrder(emailId, cartId, paymentMethod));
	}
	
	@GetMapping("/admin/orders")
	public ResponseEntity<?> getAllOrders() {
		
		 return ResponseEntity.ok(orderService.getAllOrders());
	}
	
	@GetMapping("/public/users/{emailId}/orders")
	public ResponseEntity<List<OrderDto>> getOrdersByUser(@PathVariable String emailId) {
		return ResponseEntity.ok(orderService.getOrdersByUser(emailId));
		
	}
	
	@GetMapping("/public/users/{emailId}/orders/{orderId}")
	public ResponseEntity<OrderDto> getOrderByUser(@PathVariable String emailId, @PathVariable int orderId) {
		return ResponseEntity.ok(orderService.getOrder(emailId, orderId));
		
	}
	
	@PutMapping("/admin/users/{emailId}/orders/{orderId}/orderStatus/{orderStatus}")
	public ResponseEntity<OrderDto> updateOrderByUser(@PathVariable String emailId, @PathVariable int orderId, @PathVariable String orderStatus) {
		return ResponseEntity.ok(orderService.updateOrder(emailId, orderId, orderStatus));
		
	}
	
	@GetMapping("/admin/NumberOfOrders")
	public ResponseEntity<?> getTotalUser() throws IOException {
		System.out.println("-----------------------------get users" );
		return ResponseEntity.ok(orderService.getNumberOfOrders());
	}
}
