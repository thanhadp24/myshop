package com.shopapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopapp.common.entity.Currency;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer>{
	
}
