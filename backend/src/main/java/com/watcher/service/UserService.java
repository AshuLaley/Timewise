package com.watcher.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.watcher.dto.ApiResponse;
import com.watcher.dto.UserDto;
import com.watcher.entity.User;

@Service
public interface UserService {
	
	List<UserDto> getAllUsers();
	
	UserDto getUserById(int userId);
	
	UserDto updateUser(int userId, UserDto userDTO);
	
	String deleteUser(int userId);
	
	int getCartIdByUserId(int userId);
	
	long getNumberOfUsersWithUserRoleUser();

	
//		List<UserDto> getAllUsers();
//	
//		//delete user details
//		ApiResponse deleteUserDetails(int userId);
//
//		UserDto updateUser(int userId, UserDto dto);
}
