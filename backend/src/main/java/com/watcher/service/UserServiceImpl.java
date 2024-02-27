package com.watcher.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.watcher.custom_exception.ResourceNotFoundException;
import com.watcher.dto.AddressDTO;
import com.watcher.dto.ApiResponse;
import com.watcher.dto.CartDto;
import com.watcher.dto.ProductDto;
import com.watcher.dto.UserDto;
import com.watcher.entity.Address;
import com.watcher.entity.CartItem;
import com.watcher.entity.Role;
import com.watcher.entity.User;
import com.watcher.repository.AddressRepository;
import com.watcher.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private ModelMapper mapper;
	
	public int getCartIdByUserId(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserId--"+ userId));
		return user.getCart().getId();
		}
//	@Override
//	public List<UserDto> getAllUsers() {
//		
//		return userRepository.findAll()
//				.stream()
//				.map(user -> mapper.map(user, UserDto.class))
//				.collect(Collectors.toList());
//	}

	@Override
	public List<UserDto> getAllUsers() {
	    return userRepository.findAll()
	            .stream()
	            .filter(user -> hasUserRole(user, Role.USER))
	            .map(user -> mapper.map(user, UserDto.class))
	            .collect(Collectors.toList());
	}

	private boolean hasUserRole(User user, Role roleName) {
		return user.getRole() ==roleName;
	}
	
	@Override
	public UserDto getUserById(int userId) {
		
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserId---------------------------------------"+ userId));
		System.out.println("1-----------------------"+user);

		UserDto userDTO = mapper.map(user, UserDto.class);
//		System.out.println("2-----------------------"+userDTO);
//
//		userDTO.setAddress(mapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
//		
//		System.out.println("3-----------------------"+userDTO);
//		CartDto cart = mapper.map(user.getCart(), CartDto.class);
//		System.out.println("4-----------------------"+cart);
//
//		List<ProductDto> products = user.getCart().getCartItem().stream()
//				.map(item -> mapper.map(item.getProduct(), ProductDto.class)).collect(Collectors.toList());
//		System.out.println("5-----------------------"+products);
//
//		userDTO.setCart(cart);
//
//		userDTO.getCart().setProduct(products);
//		System.out.println("6-----------------------"+userDTO);

		return userDTO;
	}

	@Override
	public UserDto updateUser(int userId, UserDto userDTO) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("UserId"+ userId));

		user.setFirstname(userDTO.getFirstname());
		user.setLastname(userDTO.getLastname());
		user.setMobile(userDTO.getMobile());
		user.setAge(userDTO.getAge());
		user.setEmail(userDTO.getEmail());

//		if (userDTO.getAddress() != null) {
//			String country = userDTO.getAddress().getCountry();
//			String state = userDTO.getAddress().getState();
//			String city = userDTO.getAddress().getCity();
//			String pincode = userDTO.getAddress().getPincode();
//			String street = userDTO.getAddress().getStreet();
//			String buildingName = userDTO.getAddress().getBuildingName();
//
//
//			Address address = addressRepository.findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(country, state,
//					city, pincode, street, buildingName);
//
//			if (address == null) {
//				address = new Address(country, state, city, pincode, street, buildingName);
//
//				address = addressRepository.save(address);
//
//				user.setAddresses(List.of(address));
//			}
//		}

		userDTO = mapper.map(user, UserDto.class);
//
//		userDTO.setAddress(mapper.map(user.getAddresses().stream().findFirst().get(), AddressDTO.class));
//
//		CartDto cart = mapper.map(user.getCart(), CartDto.class);
//
//		List<ProductDto> products = user.getCart().getCartItem().stream()
//				.map(item -> mapper.map(item.getProduct(), ProductDto.class)).collect(Collectors.toList());
//
//		userDTO.setCart(cart);
//
//		userDTO.getCart().setProduct(products);

		return userDTO;	
	}

	@Override
	public String deleteUser(int userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User With UserId"+ userId+ " Not found"));

		userRepository.deleteById(userId);
		

		return "User with userId " + userId + " deleted successfully!!!";
	}

	 public long getNumberOfUsersWithUserRoleUser() {
	        // Get the count of users with role 'USER'
	        return userRepository.countByRole(Role.USER);
	    }
	
//	@Override
//	public AuthenticationResponse register(RegisterRequest request);{
//		User user=mapper.map(reqDTO, UserEntity.class);
//		user.setPassword(encoder.encode(user.getPassword()));//pwd : encrypted using SHA
//		return mapper.map(userDao.save(user), Signup.class);
//		return ;
//	}


}
