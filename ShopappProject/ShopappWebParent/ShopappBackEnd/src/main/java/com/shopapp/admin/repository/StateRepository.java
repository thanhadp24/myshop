package com.shopapp.admin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.State;

@Repository
public interface StateRepository extends JpaRepository<State, Integer>{
	
	public List<State> findByCountryOrderByNameAsc(Country country);
}
