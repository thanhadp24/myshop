package com.shopapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Customer;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer>{

	public List<Address> findByCustomer(Customer customer);

	@Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND "
			+ "a.id = :addressId")
	public Address findByIdAndCustomer(Integer customerId, Integer addressId);
	
	@Query("DELETE Address a WHERE a.customer.id = :customerId AND "
			+ "a.id = :addressId")
	@Modifying
	public void deleteByIdAndCustomer(Integer customerId, Integer addressId);
	
	@Query("UPDATE Address a SET a.defaultForShipping = true WHERE a.id = :addressId")
	@Modifying
	public void setDefaultAddress(Integer addressId);
	
	@Query("UPDATE Address a SET a.defaultForShipping = false WHERE a.id != :defaultAddressId "
			+ "AND a.customer.id = :customerId")
	@Modifying
	public void setNoneDefault4Others(Integer defaultAddressId, Integer customerId);
	
	@Query("SELECT a FROM Address a WHERE a.customer.id = :customerId AND a.defaultForShipping = true")
	public Address findDefaultByCustomer(Integer customerId);
}
