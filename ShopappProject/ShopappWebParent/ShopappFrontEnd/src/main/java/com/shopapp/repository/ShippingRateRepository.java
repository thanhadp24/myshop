package com.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.ShippingRate;

@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer>{

	public ShippingRate findByCountryAndState(Country country, String state);
}
