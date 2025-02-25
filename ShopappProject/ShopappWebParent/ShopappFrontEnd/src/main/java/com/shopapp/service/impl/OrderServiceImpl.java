package com.shopapp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopapp.bean.CheckoutInfo;
import com.shopapp.bean.OrderReturnRequest;
import com.shopapp.common.Common;
import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.entity.order.OrderDetail;
import com.shopapp.common.entity.order.OrderTrack;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.enumm.OrderStatus;
import com.shopapp.common.enumm.PaymentMethod;
import com.shopapp.common.exception.OrderNotFoundException;
import com.shopapp.repository.OrderRepository;
import com.shopapp.service.OrderService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{

	@Autowired 
	private OrderRepository orderRepository;
	
	@Override
	public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, 
			PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
		Order newOrder = new Order();
		
		newOrder.setOrderTime(new Date());
		newOrder.setCustomer(customer);
		newOrder.setProductCost(checkoutInfo.getProductCost());
		newOrder.setSubtotal(checkoutInfo.getProductTotal());
		newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
		newOrder.setTax(0f);
		newOrder.setTotal(checkoutInfo.getPaymentTotal());
		newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
		newOrder.setDeliverDate(checkoutInfo.getDeliverDate());

		if(paymentMethod.equals(PaymentMethod.PAYPAL)) {
			newOrder.setOrderStatus(OrderStatus.PAID);
		}else {
			newOrder.setOrderStatus(OrderStatus.NEW);
		}
		newOrder.setPaymentMethod(paymentMethod);
		
		if(address == null) {
			newOrder.copyAddressFromCustomer();
		}else {
			newOrder.copyShippingAddress(address);
		}
		
		Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
		for(CartItem item: cartItems) {
			Product product = item.getProduct();
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setProduct(product);
			orderDetail.setOrder(newOrder);
			orderDetail.setQuantity(item.getQuantity());
			orderDetail.setProductCost(product.getCost() * item.getQuantity());
			orderDetail.setShippingCost(item.getShippingCost());
			orderDetail.setSubtotal(item.getSubTotal());
			orderDetail.setUnitPrice(product.getDiscountPrice());
			
			orderDetails.add(orderDetail);
		}
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setUpdatedTime(new Date());
		orderTrack.setOrder(newOrder);
		orderTrack.setStatus(OrderStatus.NEW);
		orderTrack.setNotes(OrderStatus.NEW.defaultDescription());
		
		newOrder.setOrderTracks(List.of(orderTrack));
		
		return orderRepository.save(newOrder);
	}
	
	@Override
	public Page<Order> getForCustomerByPage(int pageNum, Customer customer, 
			String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending(): sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, Common.ORDERS_PER_PAGE, sort);
		if(keyword != null) {
			return orderRepository.findAll(keyword, customer.getId(), pageable);
		}
		return orderRepository.findAll(customer.getId(), pageable);
	}
	
	@Override
	public Order get(Integer id, Customer customer) throws OrderNotFoundException {
		
		try {
			return orderRepository.findByIdAndCustomer(id, customer);
		} catch (NoSuchElementException e) {
			throw new OrderNotFoundException("Could not find any order with id: " + id);
		}
	}
	
	@Override
	public void setOrderReturnRequest(OrderReturnRequest request, Customer customer) throws OrderNotFoundException {
		Order order = orderRepository.findByIdAndCustomer(request.getOrderId(), customer);
		
		if(order == null) {
			throw new OrderNotFoundException("Order " + request.getOrderId() + " not found!!!");
		}
		
		if(order.isReturnRequested()) return;
		
		OrderTrack orderTrack = new OrderTrack();
		orderTrack.setOrder(order);
		orderTrack.setStatus(OrderStatus.RETURN_REQUESTED);
		
		String notes = "Reason: " + request.getReason();
		if(!"".equals(request.getNote())) {
			notes += ". " + request.getNote();
		}
		
		orderTrack.setNotes(notes);
		orderTrack.setUpdatedTime(new Date());
		
		order.getOrderTracks().add(orderTrack);
		order.setOrderStatus(OrderStatus.RETURN_REQUESTED);
		
		orderRepository.save(order);
	}
}
