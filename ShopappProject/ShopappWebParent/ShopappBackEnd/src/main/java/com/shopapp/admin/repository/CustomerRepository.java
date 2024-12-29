package com.shopapp.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Customer;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	@Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName, "
			+ " ' ', c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ', c.state, "
			+ " ' ', c.postalCode, ' ', c.country.name) LIKE %:keyword%")
	public Page<Customer> findAll(String keyword, Pageable pageable);
	
	public Customer findByEmail(String email);
	
	public Long countById(Integer id);
	
	@Query("UPDATE Customer c SET c.enabled = :enabled WHERE c.id = :id")
	@Modifying
	void updateEnableStatus(Integer id, boolean enabled);
}
