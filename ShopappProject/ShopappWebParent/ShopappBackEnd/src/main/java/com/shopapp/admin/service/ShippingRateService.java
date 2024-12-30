package com.shopapp.admin.service;

import java.util.List;

import com.shopapp.admin.exception.ShippingRateAlreadyExistsException;
import com.shopapp.admin.exception.ShippingRateNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.ShippingRate;

public interface ShippingRateService {
	
	void getByPage(PagingAndSortingHelper helper, int pageNum);
	
	void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException;
	
	void delete(Integer id) throws ShippingRateNotFoundException;
	
	ShippingRate get(Integer id) throws ShippingRateNotFoundException;
	
	void updateCodSupported(Integer id, boolean enabled) throws ShippingRateNotFoundException;
	
	List<Country> getAllCountries();
}
