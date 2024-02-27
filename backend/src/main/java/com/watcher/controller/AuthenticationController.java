package com.watcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.watcher.dto.AuthenticationRequest;
import com.watcher.dto.AuthenticationResponse;
import com.watcher.dto.RegisterRequest;
import com.watcher.security.AuthenticationService;
import com.watcher.security.JwtService;
import com.watcher.service.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("http://localhost:3000")
public class AuthenticationController {
//	@Autowired
//	private UserService userService;
//	
//	@Autowired
//	private JwtService jwtService;

	@Autowired
	private AuthenticationService service;

	@PostMapping("/user/register")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
		
		return ResponseEntity.ok(service.register(request));
	}
	@PostMapping("/admin/register")
	public ResponseEntity<AuthenticationResponse> adminRegister(@RequestBody RegisterRequest request){
		
		return ResponseEntity.ok(service.adminRegister(request));
	}


	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request){
		return ResponseEntity.ok(service.authenticate(request));
	}
}
