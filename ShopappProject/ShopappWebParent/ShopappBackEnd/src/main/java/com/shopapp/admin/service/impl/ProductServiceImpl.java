package com.shopapp.admin.service.impl;

import java.util.Date;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopapp.admin.common.Common;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.repository.ProductRepository;
import com.shopapp.admin.service.ProductService;
import com.shopapp.common.entity.Product;
import com.shopapp.common.exception.ProductNotFoundException;

@Service
@Transactional
public class ProductServiceImpl implements ProductService{
	@Autowired
	private ProductRepository productRepository;
	
	@Override
	public void getByPage(int pageNum, PagingAndSortingHelper helper ,Integer categoryId) {
		
		Pageable pageable = helper.createPagable(Common.PRODUCTS_PER_PAGE, pageNum);
		Page<Product> page = null;
		
		String keyword = helper.getKeyword();
		
		if(keyword != null && !keyword.isEmpty()) {
			if(categoryId != null && categoryId > 0) {
				String categoryMatch = "-" + String.valueOf(categoryId) + "-";
				page = productRepository.searchInCategory(categoryId, categoryMatch, keyword, pageable);
			}
			
			page = productRepository.findAll(keyword, pageable);
		}
		
		if(categoryId != null && categoryId > 0) {
			String categoryMatch = "-" + String.valueOf(categoryId) + "-";
			page = productRepository.findAllInCategory(categoryId, categoryMatch, pageable);
		}else {
			page = productRepository.findAll(pageable);
		}
		
		helper.updateModel(pageNum, page);
	}
	
	@Override
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return productRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new ProductNotFoundException("Could not find any product with id " + id);
		}
	}
	
	@Override
	public Product save(Product product) {
		
		if(product.getId() == null) {
			product.setCreatedAt(new Date());
		}
		
		if(product.getAlias().isEmpty() || product.getAlias() == null) {
			product.setAlias(product.getName().replaceAll(" ", "-"));
		}else {
			product.setAlias(product.getAlias().replaceAll(" ", "-"));
		}
		
		product.setUpdatedAt(new Date());
		
		return productRepository.save(product);
	}
	
	@Override
	public void saveProductPrice(Product productInForm) {
		Product productInDB = productRepository.findById(productInForm.getId()).get();
		productInDB.setCost(productInForm.getCost());
		productInDB.setPrice(productInForm.getPrice());
		productInDB.setDiscountPercent(productInForm.getDiscountPercent());
		
		productRepository.save(productInDB);
	}
	
	@Override
	public void delete(Integer id) throws ProductNotFoundException {
		Long count = productRepository.countById(id);
		if(count == null || count == 0) {
			throw new ProductNotFoundException("Could not find any product id " + id);
		}else {
			productRepository.deleteById(id);
		}
	}
	
	@Override
	public String checkUnique(String name, Integer id) {
		boolean isCreatingNew = (id == null || id == 0);
		Product product = productRepository.findByName(name);
		
		if(isCreatingNew) {
			if(product != null) {
				return "duplicate";
			}
		}else {
			if(product != null && product.getId() != id) {
				return "duplicate";
			}
		}
		
		return "ok";
	}
	
	@Override
	public void updateEnabled(Integer id, boolean status) {
		productRepository.updateEnabled(id, status);
	}
}
