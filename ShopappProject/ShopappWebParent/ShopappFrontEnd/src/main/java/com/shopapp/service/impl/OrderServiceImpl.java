package com.shopapp.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopapp.bean.CheckoutInfo;
import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.CartItem;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.entity.order.Order;
import com.shopapp.common.entity.order.OrderDetail;
import com.shopapp.common.entity.product.Product;
import com.shopapp.common.enumm.OrderStatus;
import com.shopapp.common.enumm.PaymentMethod;
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
		
		return orderRepository.save(newOrder);
	}
}
