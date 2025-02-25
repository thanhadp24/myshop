package com.shopapp.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopapp.admin.repository.RoleRepository;
import com.shopapp.admin.service.RoleService;
import com.shopapp.common.entity.Role;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	@Override
	public List<Role> getAll() {
		return roleRepository.findAll();
	}
}
