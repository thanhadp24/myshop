package com.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Customer;
import com.shopapp.common.enumm.AuthenticationType;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
	@Query("SELECT c FROM Customer c WHERE c.email = :email")
	public Customer findByEmail(String email);
	
	@Query("SELECT c FROM Customer c WHERE c.verificationCode = :code")
	public Customer findByVerificationCode(String code);
	
	@Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = :id")
	@Modifying
	public void enable(Integer id);
	
	@Query("UPDATE Customer c SET c.type = :type WHERE c.id = :customerId")
	@Modifying
	public void updateAuthenticationType(Integer customerId, AuthenticationType type);
	
	public Customer findByResetPasswordToken(String resetToken);
}
