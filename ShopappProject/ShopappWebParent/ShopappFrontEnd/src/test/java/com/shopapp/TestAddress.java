package com.shopapp;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.Customer;
import com.shopapp.repository.AddressRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestAddress {
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Test
	public void testCreate() {
		Address address = new Address();
		address.setFirstName("thanh");
		address.setLastName("tran");
		address.setPhoneNumber("1111111");
		address.setAddressLine1("Dien Ban");
		address.setCity("Quang Nam");
		address.setState("Quang Nam");
		address.setPostalCode("123411");
		address.setCountry(new Country(233));
		address.setCustomer(new Customer(1));
		
		addressRepository.save(address);
	}
	
	@Test
	public void updateDefaultAddress() {
		addressRepository.setDefaultAddress(5);
	}
	
	@Test
	public void setNoneForOthers() {
		addressRepository.setNoneDefault4Others(5, 3);
	}
	
	@Test
	public void testGetDefault() {
		Address address = addressRepository.findDefaultByCustomer(1);
		System.out.println(address);
		assertThat(address.getId()).isGreaterThan(0);
	}
}
