package com.shopapp.admin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.ShippingRate;


@Repository
public interface ShippingRateRepository extends SearchRepository<ShippingRate, Integer>{
	
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.name LIKE %:keyword% "
			+ "OR sr.state LIKE %:keyword%")
	public Page<ShippingRate> findAll(String keyword, Pageable pageable);
	
	@Query("SELECT sr FROM ShippingRate sr WHERE sr.country.id = :countryId AND "
			+ "sr.state = :state ")
	public ShippingRate findByCountryAndState(Integer countryId, String state);
	
	@Query("UPDATE ShippingRate sr SET sr.codSupported = :enabled WHERE sr.id = :id")
	@Modifying
	public void updateCodSupported(Integer id, boolean enabled);
	
	public Long countById(Integer id);
}
