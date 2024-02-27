package com.watcher.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watcher.dto.AddressDTO;
import com.watcher.dto.AddressUpdateDto;
import com.watcher.service.AddressService;


@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")

public class AddressController {

	
	@Autowired
	private AddressService addressService;

	public AddressController() {
		System.out.println("in ctor of " + getClass());
	}

	// assign address to user
	
	@PostMapping("/public/user/address/add")
	public ResponseEntity<?> assignUserAddress(
			@RequestBody AddressDTO address) {
//		System.out.println("in assign adr " + address.user.getId() + " " + address);
		return ResponseEntity.status(HttpStatus.CREATED).body(addressService.assignUserAddress( address));
	}

	// get user address
	@GetMapping("/public/user/address/{userId}")
	public ResponseEntity<?> getUserAddress(@PathVariable int userId) {
		System.out.println("in get user adr " + userId);
		// one to one with shared PK => user id is same as adr id
		return ResponseEntity.ok(addressService.getAddressDetails(userId));
	}

	// update address COMPLETE
	@PutMapping("/public/user/address/{userId}")
	public ResponseEntity<?> updateUserAddress(@PathVariable int userId,
			@RequestBody  AddressDTO address) {
		System.out.println("in complete update adr " + userId + " " + address);
		return ResponseEntity.ok()
				.body(addressService.updateUserAddress(userId, address));
	}

	// update address partial
//	@PatchMapping("/{userId}")
//	public ResponseEntity<?> updateUserAddressPartial(@PathVariable int userId,
//			@RequestBody Map<String, Object> map) throws Exception{
//		System.out.println("in partial update adr " + userId + " " + map);
//		return ResponseEntity.ok()
//				.body(addressService.patchUserAddress(userId, map));
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Autowired
//	private AddressService addressService;
//
//	@PostMapping("/add")
//	public ResponseEntity<?> addState(@RequestBody @Valid AddressDTO address) {
//
//		return new ResponseEntity<>(addressService.addAddress(address), HttpStatus.CREATED);
//	}
//
//	@DeleteMapping("/delete/{id}")
//	public ResponseEntity<?> deleteAddressById(@PathVariable Long id) {
//		return new ResponseEntity<>(addressService.deleteAddress(id), HttpStatus.OK);
//	}
//
//	@PutMapping("/update")
//	public ResponseEntity<?> updateAddressById(@RequestBody @Valid AddressUpdateDto addressDto) {
//		return new ResponseEntity<>(addressService.updateAddress(addressDto), HttpStatus.OK);
//	}

}