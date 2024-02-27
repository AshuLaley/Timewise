package com.watcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watcher.dto.UserDto;
import com.watcher.service.UserService;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:3000")

public class UserController {
	
	@Autowired
	private UserService userService;
	
	// any one should be able view the products
	@GetMapping("/view")
	public String viewProducts() {
		return "any one can view the products!!!!!!!!!";
	}
	
//	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/demo")
	public ResponseEntity<String> sayHello(){
		return ResponseEntity.ok("Hello everyOne");
	}
	
	  // any authenticated user should be able to browse the categories
		@GetMapping("/browse_categories")
		public String browseCategories() {
			return "any authenticated user should be able to browse the categories";
		}
	
		
		//1. get all user details 
		@GetMapping("/admin/getAllUser")
		public ResponseEntity<?> getUserDetails() throws IOException {
			System.out.println("-----------------------------get users" );
			return ResponseEntity.ok(userService.getAllUsers());
		}
		
		// 2. update user details
		@PutMapping("/user/{userId}")
		public ResponseEntity<?> updateUser(@PathVariable int userId, @RequestBody UserDto dto) {
			System.out.println("in update user " + userId + " " + dto);
			return ResponseEntity.ok(userService.updateUser(userId, dto));
		}
		
		// 3. delete user details
		@DeleteMapping("/admin/user/{userId}")
		public ResponseEntity<?> deleteUser(@PathVariable int userId) {
			System.out.println("in update user " + userId);
			return ResponseEntity.ok(userService.deleteUser(userId));
		}
		
		//1. get all user details 
		@GetMapping("/user/get/{userId}")
		public ResponseEntity<?> getUserById(@PathVariable int userId) throws IOException {
			System.out.println("-----------------------------get users" );
			return ResponseEntity.ok(userService.getUserById(userId));
		}
		
		@GetMapping("/user/cart/{userId}")
		public ResponseEntity<?> getCartIdByUserId(@PathVariable int userId) throws IOException {
			System.out.println("-----------------------------get users" );
			return ResponseEntity.ok(userService.getCartIdByUserId(userId));
		}
		
		@GetMapping("/admin/totalUser")
		public ResponseEntity<?> getTotalUser() throws IOException {
			System.out.println("-----------------------------get users" );
			return ResponseEntity.ok(userService.getNumberOfUsersWithUserRoleUser());
		}
		
}
