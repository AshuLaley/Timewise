package com.watcher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.watcher.entity.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddressDTO {
	
	
	private int id;
	
	private String street;
	
	private String buildingName;
	
	private String city;

	private String state;

	private String country;

	private String pincode;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	private int userId;
}
