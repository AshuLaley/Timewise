package com.watcher.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="user")
@ToString(exclude = "password")
public class User extends BaseEntity implements  UserDetails{
	
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private int userId;
	
	@NotBlank
	@Column(length = 20)
	private String firstname;
	
	@NotBlank
	@Column(length = 20)
	private String lastname;
	
	@NotBlank
	@Email(message = "Please enter a valid email address")
	@Column(unique = true)
	private String email;
	
	@NotBlank
	//@Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters")
	private String password;
	
	@NotNull
	@Column(length = 10, unique = true)
	private long mobile;
	
	@NotNull
	private int age;
	private LocalDate registerDate;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<Address> addresses = new ArrayList<>();
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private Cart cart;
	 
	
	public User(@NotBlank String firstname, @NotBlank String lastname,
			@NotBlank @Email(message = "Please enter a valid email address") String email, @NotBlank String password,
			@NotNull long mobile, @NotNull int age) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.mobile = mobile;
		this.age = age;
	}
//		//helper method
		public void addAddress(Address address) {
			System.out.println("-----------------five");
			addresses.add(address);
			address.setUser(this);
			System.out.println("-----------------final");

		}
		
		public void removeAddress(Address address) {
			addresses.remove(address);
			address.setUser(this);
		} 
		
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(role.name()));
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


}
