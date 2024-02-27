package com.watcher.service;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.watcher.custom_exception.AddressNotFoundException;
import com.watcher.custom_exception.ResourceNotFoundException;
import com.watcher.dto.AddressDTO;
import com.watcher.dto.AddressUpdateDto;
import com.watcher.dto.ApiResponse;
import com.watcher.entity.Address;
import com.watcher.entity.User;
import com.watcher.repository.AddressRepository;
import com.watcher.repository.UserRepository;
import java.util.List;



@Service
public class AddressServiceImpl implements AddressService{

	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@Override
	public AddressDTO getAddressDetails(int userId) {
		
		 List<Address> addresses = addressRepository.findByUserId(userId);

		    if (addresses.isEmpty()) {
		        throw new AddressNotFoundException("No address found for the user");
		    }
		    Address address = addresses.get(addresses.size() - 1);

		    return mapper.map(address, AddressDTO.class);
	}
	
	@Override
	public AddressDTO assignUserAddress( AddressDTO addressDto) {
	
		Address address=mapper.map(addressDto, Address.class);

		User user=userRepository.findById(addressDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Invalid Customer ID"));
		user.addAddress(address);

		addressRepository.save(address);
		

		
		return  addressDto;
	}

//	@Override
//	public ApiResponse assignUserAddress(Long userId, AddressDTO address) {
//		
//		User user = userRepository.findById(userId).orElseThrow(
//				()-> new ResourceNotFoundException("Invalid User ID!!!"));
//		
//		Address addressEntity = mapper.map(address, Address.class);
//		addressEntity.setUser(user);
//		addressRepository.save(addressEntity);
//		return new ApiResponse("Assigned new address to User , " + user.getFirstname());
//	}

	@Override
	public ApiResponse updateUserAddress(int userId, AddressDTO addressDto) {
		
		// Retrieve the user based on userId
	    User user = userRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

	    // Retrieve the Address entity associated with the user
	    Address addressEntity = user.getAddresses().stream()
	            .filter(address -> address.getUser().getId()==addressDto.getUserId())
	            .findFirst()
	            .orElseThrow(() -> new AddressNotFoundException("Address not found for user with id: " + userId));

	    // Map the properties from AddressDTO to Address entity
	    mapper.map(addressDto, addressEntity);

	    // Save the changes if needed (depending on cascading settings)
	    addressRepository.save(addressEntity);

	    return new ApiResponse("Updated address for the user");

	}
	
	
//	@Override
//	public AddressDTO patchUserAddress(int userId, Map<String, Object> map) {
//		Address address = addressRepository.findById(userId)
//				.orElseThrow(() -> new ResourceNotFoundException("Invalid Address Id!!!!"));
//			
//		mapper.map(map,address);
//		
//		System.out.println("updated address " + address);
//		return mapper.map(address, AddressDTO.class);
//	}

//	@Override
//	public AddressDTO getAddressByUserId(Long userId) {
//		
//		return mapper.map(addressRepository.findById(userId).orElseThrow(
//				() -> new ResourceNotFoundException("Invalid User  Id Or Address not yet assigned !!!!")),
//				AddressDTO.class);	
//	}
//	
//	@Override
//	public ApiResponse addAddress(@Valid AddressDTO addressDto) {
//		Address address=mapper.map(addressDto, Address.class);
//		User user=userRepository.findById(addressDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Invalid Customer ID"));
//		addressRepository.save(address);
//		user.addAddress(address);
//		return  new ApiResponse(" Address Added Successfully...");
//	}
//
//	@Override
//	public ApiResponse deleteAddress(Long id) {
//		Address address=addressRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invalid address ID"));
//		addressRepository.delete(address);
//		return new ApiResponse(" Address deleted Successfully...");
//	}
//
//	@Override
//	public ApiResponse updateAddress(AddressUpdateDto addressDto) {
//		
//		Address address = addressRepository.findById(addressDto.getAddressId())
//                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressDto.getAddressId()));
//		
//	        if (addressDto.getAddress() != null) {
//	            address.setAddress(addressDto.getAddress());
//	        }
//	        if (addressDto.getCity() != null) {
//	        	address.setCity(addressDto.getCity());
//	        }
//	        if (addressDto.getState() != null) {
//	            address.setState(addressDto.getState());
//	        }
//	        if (addressDto.getCountry() != null) {
//	            address.setCountry(addressDto.getCountry());
//	        }
//
//	        if (addressDto.getZipCode() != null) {
//	            address.setZipCode(addressDto.getZipCode());
//	        }
//
//	        if ( addressDto.getUserId() != null) {
//	        	User user=userRepository.findById(addressDto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("Invalid User ID"));
//	        	user.addAddress(address);
//	        }
//
//		return new ApiResponse(" Address updated Successfully...");
//	}	

}
