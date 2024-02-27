package com.watcher.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.watcher.service.UserService;
import com.watcher.service.UserServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	
	@Autowired
	private JwtService jwtService;
	
//	@Autowired
//	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("Authorization");
		
//		System.out.println(requestTokenHeader);
		
		String username=null;
		String jwtToken=null;
		
		if(requestTokenHeader != null  && requestTokenHeader.startsWith("Bearer "))
		{
	//		try {
				jwtToken = requestTokenHeader.substring(7);
				username = jwtService.extractUserName(jwtToken);
				
//			}catch (ExpiredJwtException e) {
//				
//				e.printStackTrace();
//				System.err.println("jwt token has expired");
//			}
			
			
		}
		
		
		// validate token

		if(username != null && SecurityContextHolder.getContext().getAuthentication()== null)
		{
			final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
			
			if(jwtService.validateToken(jwtToken, userDetails))
			{
				//token is valid
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken  = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));			
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		
	}
	
	filterChain.doFilter(request, response);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	@Override
//	protected void doFilterInternal( 
//			@NonNull HttpServletRequest request, 
//			@NonNull HttpServletResponse response, 
//			@NonNull FilterChain filterChain ) throws ServletException, IOException {
//
//		if (request.getServletPath().contains("/api/v1/auth")) {
//		      filterChain.doFilter(request, response);
//		      return;
//		    }
//		
//		final String authHeader = request.getHeader("Authorization");
//		final String jwtToken;
//		final String userEmail;
//		
//		if(authHeader == null && !authHeader.startsWith("Bearer ")) {
//		  filterChain.doFilter(request, response);
//			return;
//		}
//		System.out.println("---------------------------one");
//		jwtToken = authHeader.substring(7);
//		System.out.println("---------------------------two");
//
//		  userEmail = jwtService.extractUserName(jwtToken);
//		  
//	    if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//		  UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(userEmail);
//		  
//		  if(jwtService.isTokenValid(jwtToken, userDetails)) {
//			  UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities() );
//		  
//		  authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//		  
//		  SecurityContextHolder.getContext().setAuthentication(authToken);
//		  }
//	  
//	   }
//		  
//		  filterChain.doFilter(request, response);
		}

}
