package com.watcher.service;

import java.util.Map;

import com.watcher.dto.AddressDTO;
import com.watcher.dto.AddressUpdateDto;
import com.watcher.dto.ApiResponse;
import com.watcher.entity.Address;

public interface AddressService {
	
	AddressDTO assignUserAddress(  AddressDTO address);		

	//	get address details
	AddressDTO getAddressDetails(int userId);
	
	//update User address : complete update
	ApiResponse updateUserAddress( int userId,  AddressDTO address);

	
	
	//update User address : partial update
//	AddressDTO patchUserAddress( int userId, Map<String, Object> map);

// assign User address
//		ApiResponse assignUserAddress( Long userId,  AddressDTO address);		
//	AddressDTO getAddressByUserId(Long userId);
//	ApiResponse addAddress( AddressDTO address);
//
//	ApiResponse deleteAddress(Long id);
//
//	ApiResponse  updateAddress(AddressUpdateDto address);
}
