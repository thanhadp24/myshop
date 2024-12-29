package com.shopapp.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
	
}
