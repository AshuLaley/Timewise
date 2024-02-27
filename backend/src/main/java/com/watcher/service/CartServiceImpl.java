package com.watcher.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.FetchMode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watcher.custom_exception.ApiException;
import com.watcher.custom_exception.CartNotFoundException;
import com.watcher.custom_exception.ProductNotFoundException;
import com.watcher.custom_exception.ResourceNotFoundException;
import com.watcher.dto.CartDto;
import com.watcher.dto.ProductDto;
import com.watcher.entity.Cart;
import com.watcher.entity.CartItem;
import com.watcher.entity.Product;
import com.watcher.entity.User;
import com.watcher.repository.CartItemRepository;
import com.watcher.repository.CartRepository;
import com.watcher.repository.ProductRepository;
import com.watcher.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional 
public class CartServiceImpl implements CartService {

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public String addProductToCart(int cartId, int productId, int quantity) {
		
		Cart cart = cartRepository.findById(cartId)
					.orElseThrow(()-> new CartNotFoundException("cartId"+cartId));
		
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("productId"+ productId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
		
		if (cartItem != null) {
			return "Product " + product.getTitle() + " already exists in the cart";
			//throw new ApiException("Product " + product.getTitle() + " already exists in the cart");
		}

		if (product.getQuantity() == 0) {
			throw new ApiException(product.getTitle() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new ApiException("Please, make an order of the " + product.getTitle()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}
		
		CartItem newCartItem = new CartItem();

		newCartItem.setProduct(product);
		newCartItem.setCart(cart);
		newCartItem.setQuantity(quantity);
		newCartItem.setDiscount(product.getDiscountedPrice());
		newCartItem.setPrice(product.getPrice());
		System.out.println(newCartItem.getPrice()+"-------------------"+product.getPrice());
		cartItemRepository.save(newCartItem);

		product.setQuantity(product.getQuantity() - quantity);
		
		cart.addCart(newCartItem);
		cart.setTotalPrice(cart.getTotalPrice() + (product.getDiscountedPrice() * quantity));

		CartDto cartDto = mapper.map(cart, CartDto.class);

		cartDto.setProduct(convertProducts(cart.getCartItem()));
		cartDto.setUserId(cart.getUser().getId());
		cartDto.setName(cart.getUser().getFirstname());
		
		return "added";
	}

	@Override
	public List<CartDto> getAllCarts() {
	
		  List<Cart> carts = cartRepository.findAll();

		    // Convert carts to CartDtos with enhanced logic
		    List<CartDto> cartDtos = carts.stream()
		            .map(cart -> {
		                CartDto cartDto = new CartDto();
		                cartDto.setId(cart.getId());
		                cartDto.setUserId(cart.getUser().getId());
		                cartDto.setName(cart.getUser().getFirstname());
		                cartDto.setTotalPrice(cart.getTotalPrice());
		                // Convert cart items and products using appropriate mapping (e.g., ModelMapper)
		                cartDto.setProduct(convertProducts(cart.getCartItem()));
		                return cartDto;
		            })
		            .collect(Collectors.toList());
		    
		    return cartDtos;

		
	}
	
//	@Override
//	public CartDto getCartById(int cartId) {
//		System.out.println("-------"+1);
//		Cart cart = cartRepository.findById(cartId)
//				.orElseThrow(() -> new CartNotFoundException("Cart Not Found"));
//		System.out.println(cart);
//
//		CartDto cartDto = mapper.map(cart, CartDto.class);
//		System.out.println(cartDto);
//		cartDto.setProduct(convertProducts(cart.getCartItem()));
//		cartDto.setUserId(cart.getUser().getId());
//		cartDto.setName(cart.getUser().getFirstname());
//	
//		return cartDto;
//	}
	
	@Override
	public CartDto getCartById(int cartId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
		
		            
		CartDto cartDto = mapper.map(cartRepository.findById(cartId), CartDto.class);
		
		List<ProductDto> products = cart.getCartItem().stream()
				.map(p -> mapper.map(p.getProduct(), ProductDto.class)).collect(Collectors.toList());

		cartDto.setProduct(products);

		return cartDto;
	}

	
	@Override
	public CartDto updateProductQuantityInCart(int cartId, int productId, int quantity) {
		
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("CartId"+ cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("ProductId"+ productId));

		if (product.getQuantity() == 0) {
			throw new ApiException(product.getTitle() + " is not available");
		}

		if (product.getQuantity() < quantity) {
			throw new ApiException("Please, make an order of the " + product.getTitle()
					+ " less than or equal to the quantity " + product.getQuantity() + ".");
		}

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);
		
		if (cartItem == null) {
			throw new ApiException("Product " + product.getTitle() + " not available in the cart!!!");
		}
		
		double cartPrice = cart.getTotalPrice() - (cartItem.getPrice() * cartItem.getQuantity());

		product.setQuantity(product.getQuantity() + cartItem.getQuantity() - quantity);

		cartItem.setPrice(product.getPrice());
		cartItem.setQuantity(quantity);
		cartItem.setDiscount(product.getDiscountedPrice());

		cart.setTotalPrice(cartPrice + (cartItem.getPrice() * quantity));
		cartItem = cartItemRepository.save(cartItem);

		CartDto cartDto = mapper.map(cart, CartDto.class);

		cartDto.setProduct(convertProducts(cart.getCartItem()));
		cartDto.setUserId(cart.getUser().getId());
		cartDto.setName(cart.getUser().getFirstname());

		return cartDto;
	}

	@Override
	public void updateProductInCarts(int cartId, int productId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("CartId"+ cartId));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException("ProductId"+ productId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new ApiException("Product " + product.getTitle() + " not available in the cart!!!");
		}

		double cartPrice = cart.getTotalPrice() - (cartItem.getPrice() * cartItem.getQuantity());

		cartItem.setPrice(product.getPrice());

		cart.setTotalPrice(cartPrice + (cartItem.getPrice() * cartItem.getQuantity()));

		cartItem = cartItemRepository.save(cartItem);
		
	}

	@Override
	public String deleteProductFromCart(int cartId, int productId) {
		Cart cart = cartRepository.findById(cartId)
				.orElseThrow(() -> new CartNotFoundException("CartId"+cartId));

		CartItem cartItem = cartItemRepository.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new ResourceNotFoundException("ProductId"+productId+"Not found in Cart");
		}

		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getPrice() * cartItem.getQuantity()));

		Product product = cartItem.getProduct();
		product.setQuantity(product.getQuantity() + cartItem.getQuantity());

		cartItemRepository.deleteCartItemByProductIdAndCartId(cartId, productId);

		return "Product " + cartItem.getProduct().getTitle() + " removed from the cart !!!";
	}
	
	private List<ProductDto> convertProducts(List<CartItem> cartItems) {
	    return cartItems.stream()
	            .map(cartItem -> {
	                ProductDto productDto = mapper.map(cartItem.getProduct(), ProductDto.class);
	                productDto.setQuantity(cartItem.getQuantity()); // Add quantity from CartItem
	                return productDto;
	            })
	            .collect(Collectors.toList());
	}

	public int getCartByUserEmail(String email) {
		
			User user =	userRepository.findByEmail(email)
					.orElseThrow(()-> new ResourceNotFoundException("User Not Found"));
		return cartRepository.findByUserId(user.getId()).getId();
	}

}
