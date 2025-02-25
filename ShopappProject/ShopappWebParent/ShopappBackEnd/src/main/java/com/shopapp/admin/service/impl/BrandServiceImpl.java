package com.shopapp.admin.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopapp.admin.common.Common;
import com.shopapp.admin.exception.BrandNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.repository.BrandRepository;
import com.shopapp.admin.service.BrandService;
import com.shopapp.common.entity.Brand;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	private BrandRepository brandRepository;

	@Override
	public List<Brand> getAll() {
		return brandRepository.findAll();
	}
	
	@Override
	public void getByPage(int pageNum, PagingAndSortingHelper helper) {
		helper.listEntities(pageNum, Common.BRANDS_PER_PAGE, brandRepository);
	}
	
	@Override
	public Brand save(Brand brand) {
		return brandRepository.save(brand);
	}

	@Override
	public Brand get(Integer id) throws BrandNotFoundException {
		try {
			return brandRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new BrandNotFoundException("Could not find any brand with id " + id);
		}
	}

	@Override
	public void delete(Integer id) throws BrandNotFoundException {
		Long count = brandRepository.countById(id);
		
		if(count == null || count == 0) {
			throw new BrandNotFoundException("Cound not find any brand with id " +  id);
		}

		brandRepository.deleteById(id);
	}

	@Override
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Brand brand = brandRepository.findByName(name);
		if(isCreatingNew) {
			if(brand != null) {
				return "Duplicate";
			}
		}else {
			if(brand != null && id != brand.getId()) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
	
	
}
