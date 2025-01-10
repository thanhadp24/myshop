package com.shopapp.admin.service.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopapp.admin.common.Common;
import com.shopapp.admin.exception.OrderNotFoundException;
import com.shopapp.admin.helper.PagingAndSortingHelper;
import com.shopapp.admin.repository.CountryRepository;
import com.shopapp.admin.repository.OrderRepository;
import com.shopapp.admin.service.OrderService;
import com.shopapp.common.entity.Country;
import com.shopapp.common.entity.order.Order;

@Service
public class OrderServiceImpl implements OrderService{

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Override
	public void getByPage(int pageNum, PagingAndSortingHelper helper) {
		String sortDir = helper.getSortDir();
		String sortField = helper.getSortField();
		String keyword = helper.getKeyword();
		
		Sort sort = null;
		
		if("destination".equals(sortField)) {
			sort = Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		}else {
			sort = Sort.by(sortField);
		}

		sort = sortDir.equals("asc") ? sort.ascending(): sort.descending();
		Pageable pageable = PageRequest.of(pageNum - 1, Common.ORDERS_PER_PAGE, sort);
		
		Page<Order> page = null;
		if(keyword != null) {
			page = orderRepository.findAll(keyword, pageable);
		}else {
			page = orderRepository.findAll(pageable);
		}
		
		helper.updateModel(pageNum, page);
	}
	
	@Override
	public void save(Order orderInForm) {
		Order orderInDb = orderRepository.findById(orderInForm.getId()).get();
		
		orderInForm.setOrderTime(new Date());
		orderInForm.setCustomer(orderInDb.getCustomer());
		orderInForm.setDeliverDate(orderInDb.getDeliverDate());
		
		orderRepository.save(orderInForm);
	}
	
	@Override
	public Order get(Integer id) throws OrderNotFoundException {
		 try {
			 return orderRepository.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new OrderNotFoundException("Could not find any order with id: " + id);
		}
	}
	
	@Override
	public void delete(Integer id) throws OrderNotFoundException {
		Long count = orderRepository.countById(id);
		if(count == null || count == 0) {
			throw new OrderNotFoundException("Could not find any order with id: " + id);
		}
		orderRepository.deleteById(id);
	}
	
	@Override
	public List<Country> getAllCountries() {
		return countryRepository.findAllByOrderByNameAsc();
	}
}
