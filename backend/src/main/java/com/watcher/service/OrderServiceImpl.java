package com.watcher.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watcher.custom_exception.ApiException;
import com.watcher.custom_exception.CartNotFoundException;
import com.watcher.custom_exception.OrderNotFoundException;
import com.watcher.custom_exception.ResourceNotFoundException;
import com.watcher.dto.OrderDto;
import com.watcher.dto.OrderItemDto;
import com.watcher.dto.ProductDto;
import com.watcher.entity.Cart;
import com.watcher.entity.CartItem;
import com.watcher.entity.Order;
import com.watcher.entity.OrderItem;
import com.watcher.entity.Payment;
import com.watcher.entity.Product;
import com.watcher.entity.User;
import com.watcher.repository.CartItemRepository;
import com.watcher.repository.CartRepository;
import com.watcher.repository.OrderItemRepository;
import com.watcher.repository.OrderRepository;
import com.watcher.repository.PaymentRepository;
import com.watcher.repository.UserRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public CartRepository cartRepository;

	@Autowired
	public OrderRepository orderRepository;

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	CartItemRepository cartItemRepository;
	@Autowired
	public ModelMapper mapper;
	
	@Override
	public OrderDto placeOrder(String emailId, int cartId, String paymentMethod) {
		System.out.println("1--------------------------"+cartId);
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(()-> new CartNotFoundException("Cart Not Found"));


		Order order = new Order();
		
		order.setEmail(emailId);
		order.setOrderDate(LocalDate.now());

		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);

		payment = paymentRepository.save(payment);

		order.setPayment(payment);

		Order savedOrder = orderRepository.save(order);
		
		List<CartItem> cartItems = cart.getCartItem();
		
		if (cartItems.size() == 0) {
			throw new ApiException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getPrice());
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);
		}

		orderItems = orderItemRepository.saveAll(orderItems);

		cart.getCartItem().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cartId, item.getProduct().getId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDto orderDTO = mapper.map(savedOrder, OrderDto.class);
		
//		orderItems.forEach(item -> orderDTO.getOrderItems().add(mapper.map(item, OrderItemDto.class)));
//		System.out.println("5--------------------------"+orderItems);

		User user = userRepository.findByEmail(emailId)
				.orElseThrow();
//		orderDTO.setOrderProduct(convertProducts(order.getOrderItems()));
		orderDTO.setEmail(order.getEmail());

		deleteAllCartItemsByCartId(cart.getId());
		
		return orderDTO;
	}
	public void deleteAllCartItemsByCartId(int cartId) {
        System.out.println("------------------------------");
		cartItemRepository.deleteByCartId(cartId);
    }
	@Override
	public OrderDto getOrder(String emailId, int orderId) {
		
		Order order = orderRepository.findOrderByEmailAndId(emailId, orderId);

		if (order == null) {
			throw new OrderNotFoundException("OrderId"+orderId);
		}

		OrderDto orderDTO = mapper.map(order, OrderDto.class);
		 
		 User user = userRepository.findByEmail(emailId)
					.orElseThrow();
			System.out.println("-----------------------------"+user.getId());
			orderDTO.setEmail(order.getEmail());

			return orderDTO;
	}

	@Override
	public List<OrderDto> getOrdersByUser(String emailId) {
		List<Order> orders = orderRepository.findAllByEmail(emailId);

		List<OrderDto> orderDto = orders.stream().map(order -> mapper.map(order, OrderDto.class))
				.collect(Collectors.toList());

		if (orderDto.size() == 0) {
			throw new ApiException("No orders placed yet by the user with email: " + emailId);
		}

		return orderDto;
	}

	@Override
	public List<OrderDto> getAllOrders() {
	
		 List<Order> orders = orderRepository.findAll();

		    List<OrderDto> orderDtoList = orders.stream().map(order -> {
		        OrderDto orderDto = mapper.map(order, OrderDto.class);
		        List<OrderItemDto> orderItemDtoList = order.getOrderItems().stream()
		                .map(orderItem -> mapper.map(orderItem, OrderItemDto.class))
		                .collect(Collectors.toList());
		        orderDto.setOrderItems(orderItemDtoList);
		        return orderDto;
		    }).collect(Collectors.toList());
		    return orderDtoList;
//		List<Order> orders = orderRepository.findAll();
//
//		
//		List<OrderDto> orderDto = orders.stream().map(order -> mapper.map(order, OrderDto.class))
//				.collect(Collectors.toList()); 
//		
//		return orderDto;
	}

	@Override
	public OrderDto updateOrder(String emailId, int orderId, String orderStatus) {
		
		Order order = orderRepository.findOrderByEmailAndId(emailId, orderId);

		if (order == null) {
			throw new OrderNotFoundException("OrderId"+orderId);
		}

		order.setOrderStatus(orderStatus);

		return mapper.map(order, OrderDto.class);
	}

	private List<ProductDto> convertProducts(List<OrderItem> orderItems) {
	    return orderItems.stream()
	            .map(orderItem -> {
	                ProductDto productDto = mapper.map(orderItem.getProduct(), ProductDto.class);
	                productDto.setQuantity(orderItem.getQuantity()); // Add quantity from CartItem
	                return productDto;
	            })
	            .collect(Collectors.toList());
	}
	
	 public long getNumberOfOrders() {
	        // Using the count() method from JpaRepository to get the number of orders
	        return orderRepository.count();
	    }
}
