package com.watcher.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.watcher.entity.Address;
import com.watcher.entity.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

	Address findByCountryAndStateAndCityAndPincodeAndStreetAndBuildingName(String country, String state, String city,
			String pincode, String street, String building);
//	@Query("SELECT a FROM Address a WHERE a.user.id = :userId")
	List<Address> findByUserId(int userId);
}
