package com.shopapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Integer>{
	
	public List<Country> findAllByOrderByNameAsc();
	
	@Query("SELECT c FROM Country c WHERE c.code = :code")
	public Country findByCode(String code);
}
