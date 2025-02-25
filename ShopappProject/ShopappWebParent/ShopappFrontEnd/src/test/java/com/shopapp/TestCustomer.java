package com.shopapp;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.enumm.AuthenticationType;
import com.shopapp.repository.CustomerRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class TestCustomer {
	
	@Autowired
	private CustomerRepository repository;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreate() {
		Country country = entityManager.find(Country.class, 234);
		
		Customer customer = new Customer();
		customer.setEmail("c1@gmail.com");
		customer.setFirstName("thanh");
		customer.setLastName("dep trai");
		customer.setPassword("111");
		customer.setAddressLine1("82 - Nguyen Luong Bang");
		customer.setCountry(country);
		customer.setCity("Da Nang");
		customer.setState("Quan Lien Chieu");
		customer.setCreatedAt(new Date());
		customer.setPostalCode("12223");
		customer.setPhoneNumber("12232323");
		
		repository.save(customer);
	}
	
	@Test
	public void updateAuthenticationType() {
		repository.updateAuthenticationType(1,AuthenticationType.FACEBOOK);
	}
}
