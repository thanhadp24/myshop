package com.shopapp.admin.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopapp.admin.common.Common;
import com.shopapp.admin.exception.ShippingRateAlreadyExistsException;
import com.shopapp.admin.exception.ShippingRateNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.repository.CountryRepository;
import com.shopapp.admin.repository.ShippingRateRepository;
import com.shopapp.admin.service.ShippingRateService;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.ShippingRate;

@Service
@Transactional
public class ShippingRateServiceImpl implements ShippingRateService {

	@Autowired
	private ShippingRateRepository rateRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Override
	public void getByPage(PagingAndSortingHelper helper, int pageNum) {
		helper.listEntities(pageNum, Common.SHIPPING_RATES_PER_PAGE, rateRepository);
	}

	@Override
	public void save(ShippingRate rateInForm) throws ShippingRateAlreadyExistsException {
		ShippingRate rateInDb = rateRepository
				.findByCountryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewModel = rateInForm.getId() == null 
				&& rateInDb != null;
		boolean foundDifferenceExistingRateInEditMode = rateInForm.getId() != null && 
				rateInDb != null;
		
		if(foundDifferenceExistingRateInEditMode || foundExistingRateInNewModel) {
			throw new ShippingRateAlreadyExistsException("There's already a rate for "
					+ "the destination " + rateInForm.getCountry().getName() + ", " + rateInForm.getRate());
		}
		
		rateRepository.save(rateInForm);
	}
	
	@Override
	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		try {
			return rateRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ShippingRateNotFoundException("Could not found any shipping rate with id "+ id);
		}
	}

	@Override
	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long countById = rateRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new ShippingRateNotFoundException("Could not found any shipping rate with id "+ id);
		}
		
		rateRepository.deleteById(id);
	}

	@Override
	public void updateCodSupported(Integer id, boolean enabled) throws ShippingRateNotFoundException {
		Long countById = rateRepository.countById(id);
		if(countById == null || countById == 0) {
			throw new ShippingRateNotFoundException("Could not found any shipping rate with id "+ id);
		}
		
		rateRepository.updateCodSupported(id, enabled);
	}
	
	@Override
	public List<Country> getAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}

}
