package com.watcher.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.watcher.service.UserServiceImpl;

import lombok.RequiredArgsConstructor;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import  com.watcher.entity.Role;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	public static final String[] PUBLIC_URLS = {
			"/api/v1/auth/**",
			"/view",
			"/v*/api-doc*/**",
			"/swagger-ui/**",
			"/api/user/**",
			
			"/api/admin/**",
			"/api/public/**",
			"api/admin/**"
//			"/order/**",
//			"/address/**",
//			"/cart/**",
//			"/product/**"
	};
	
	public static final String[] ADMIN_URLS= {
//			"/api/admin/**",
//			"/api/product/update/**",
//			"/api/product/add",
//			"/api/product/delete/**",
//			"/api/order/admin/**",
//			"/api/cart/admin/**",
//			"/api/user/demo"
	};
	public static final String[] USER_URLS= {
//			"/api/public/**",
//			"/api/user/**"
	};

//	private AuthenticationProvider authenticationProvider;
//	@Autowired
//	private UserServiceImpl userServiceImpl;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		System.out.println(Role.ADMIN.name()+"---------------------------"+"ADMIN");
			
		http
		 	.csrf(AbstractHttpConfigurer::disable)
         	.authorizeHttpRequests(req ->
             req.requestMatchers(PUBLIC_URLS)
				.permitAll()
//				.requestMatchers(USER_URLS).hasAnyAuthority("USER", "ADMIN")
//				.requestMatchers(ADMIN_URLS).hasAuthority("ADMIN")
				.anyRequest()
				.authenticated()
			)
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
			.authenticationProvider(authenticationProvider())
			.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);
			
			return http.build();

	}
		
	// 
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider daoAuthProvider =new DaoAuthenticationProvider();
		daoAuthProvider.setUserDetailsService(this.customUserDetailsService);
		daoAuthProvider.setPasswordEncoder(passwordEncoder());;
		return daoAuthProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception
	{
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
		
//		http.cors()
//		.and().
//		//disable CSRF token generation n verification
//		csrf()	.disable()
//		.exceptionHandling().authenticationEntryPoint(authEntry).
//		and().
//		authorizeRequests()
//		.antMatchers("/products/view","/users/signup","/users/signin",
//				"/v*/api-doc*/**","/swagger-ui/**").permitAll()
//		// only required for JS clnts (react / angular) : for the pre flight requests
//		.antMatchers(HttpMethod.OPTIONS).permitAll()
//		.antMatchers("/products/purchase").hasRole("CUSTOMER")
//		.antMatchers("/products/add").hasRole("ADMIN")
//		.anyRequest().authenticated()
//		.and()
//		//to tell spring sec : not to use HttpSession to store user's auth details
//		.sessionManagement()
//		.sessionCreationPolicy(SessionCreationPolicy.STATELESS).
//		and()
//		//inserting jwt filter before sec filter
//		.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//	
//		return http.build();
	
		
		
//		 http
//		 .csrf((csrf) -> csrf.disable())
//		 .sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//         .authorizeHttpRequests((auth) -> auth
//             .requestMatchers("/api/v1/auth/**","/swagger-ui/**").permitAll()
//             .anyRequest().authenticated())
//         .authenticationProvider(authenticationProvider())
//		 .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//		
//		 return http.build();
//	}


}

/*



*/

//deprecated:-
/*
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
		.csrf()
		.disable()
		.authorizeHttpRequests()
		.requestMatchers(null)
		.permitAll()
		.anyRequest()
		.authenticated()
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwt)
	}
*/