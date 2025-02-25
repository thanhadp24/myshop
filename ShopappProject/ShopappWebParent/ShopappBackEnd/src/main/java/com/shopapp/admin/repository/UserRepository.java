package com.shopapp.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.User;

@Repository
public interface UserRepository extends SearchRepository<User, Integer>{
	
	@Query("SELECT u FROM User u WHERE u.email= :email")
	public User getUserByEmail(String email);
	
	public Long countById(Integer id);
	
	@Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :id")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ', u.lastName) LIKE %:keyword%")
	public Page<User> findAll(String keyword, Pageable pageable);
}
