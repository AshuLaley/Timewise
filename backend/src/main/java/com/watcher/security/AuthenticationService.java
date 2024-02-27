package com.watcher.security;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.watcher.dto.AuthenticationRequest;
import com.watcher.dto.AuthenticationResponse;
import com.watcher.dto.RegisterRequest;
import com.watcher.dto.UserDto;
import com.watcher.entity.Cart;
import com.watcher.entity.Role;
import com.watcher.entity.User;
import com.watcher.repository.UserRepository;
import com.watcher.service.CartServiceImpl;
import com.watcher.service.UserService;

@Service
public class AuthenticationService {
	
	@Autowired
	  private  UserRepository userRepository;
	
	@Autowired
	  private  UserService userService;
	@Autowired
	  private  PasswordEncoder passwordEncoder;
	@Autowired
	  private  JwtService jwtService;
	@Autowired
	  private  AuthenticationManager authenticationManager;
	@Autowired
	private CartServiceImpl cartServiceImpl;
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	  
	  public AuthenticationResponse register(RegisterRequest request) {
	      var user = User.builder()
	                  .firstname(request.getFirstname())
	                  .lastname(request.getLastname())
	                  .email(request.getEmail())
	                  .password(passwordEncoder.encode(request.getPassword()))
	                  .mobile(request.getMobile())
	                  .age(request.getAge())
	                  .registerDate(LocalDate.now())
	                  .role(Role.USER)
	                  .build();
            
	        Cart cart = new Cart();
	        cart.setUser(user);
			user.setCart(cart);
	      user = userRepository.save(user);
	      var jwtToken = jwtService.generateToken(user);
	      return AuthenticationResponse.builder().token(jwtToken).build();
	  }
	  
	  public AuthenticationResponse authenticate(AuthenticationRequest request) {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
						request.getEmail(), 
						request.getPassword())					
					);  
			var user = userRepository.findByEmail(request.getEmail())
					.orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));;
					
			var jwtToken = jwtService.generateToken(user);
			
			UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
			AuthenticationResponse response = new AuthenticationResponse();
			
			response.setToken(jwtToken);
			response.setUser(mapper.map((User) userDetails, UserDto.class));
			
			response.setCartId(cartServiceImpl.getCartByUserEmail(request.getEmail()));
			return response;
		  }
/*
	  public AuthenticationResponse signin(SignInRequest request) {
	      authenticationManager.authenticate(
	              new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
	      var emp = userRepository.findByUsername(request.getUsername())
	              .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
	      var jwt = jwtService.generateToken(emp);
	      return JwtAuthenticationResponse.builder().token(jwt).build();
	  }
	*/

	public AuthenticationResponse adminRegister(RegisterRequest request) {
		 var user = User.builder()
                 .firstname(request.getFirstname())
                 .lastname(request.getLastname())
                 .email(request.getEmail())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .mobile(request.getMobile())
                 .age(request.getAge())
                 .registerDate(LocalDate.now())
                 .role(Role.ADMIN)
                 .build();
       
       Cart cart = new Cart();
       cart.setUser(user);
		user.setCart(cart);
     user = userRepository.save(user);
     var jwtToken = jwtService.generateToken(user);
     return AuthenticationResponse.builder().token(jwtToken).build();
	}  
}
