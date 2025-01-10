package com.shopapp.common.entity.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shopapp.common.entity.AbstractAddress;
import com.shopapp.common.entity.Address;
import com.shopapp.common.entity.Customer;
import com.shopapp.common.enumm.OrderStatus;
import com.shopapp.common.enumm.PaymentMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "orders")
public class Order extends AbstractAddress{

	@Column(length = 45, nullable = false)
	private String country;
	
	private Date orderTime;
	
	@Column(name = "shipping_cost")
	private float shippingCost;
	
	@Column(name = "product_cost")
	private float productCost;
	
	private float subtotal;
	private float tax;
	private float total;
	
	@Column(name = "deliver_days")
	private int deliverDays;
	
	@Column(name = "deliver_date")
	private Date deliverDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();
	
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy(value = "updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();
	
	public Order() {
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getTax() {
		return tax;
	}

	public void setTax(float tax) {
		this.tax = tax;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}
	
	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}
	
	@Transient
	public String getDeliverDateOnForm() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(deliverDate);
	}
	
	public void setDeliverDateOnForm(String dateString) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			this.deliverDate = df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Transient
	public String getDestination() {
		String destination = city + ", ";
		if(state != null && !state.isEmpty()) destination += state;
		destination += ", " + country;
		return destination;
	}
	
	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setCity(customer.getCity());
		setState(customer.getState());
		setCountry(customer.getCountry().getName());
		setPostalCode(customer.getPostalCode());
	}

	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setCity(address.getCity());
		setState(address.getState());
		setCountry(address.getCountry().getName());
		setPostalCode(address.getPostalCode());
	}
	
	@Transient
	public String getShippingAddress() {
		StringBuilder address = new StringBuilder(firstName);
		 
		if(lastName != null && !lastName.isEmpty()) address.append(" " + lastName);
		
		if(!addressLine1.isEmpty()) address.append(".Address: " + addressLine1);
		
		if(addressLine2 != null && !addressLine2.isEmpty()) address.append(" - " + addressLine2);
		
		if(!city.isEmpty()) address.append(", " + city);
		
		if(state != null && !state.isEmpty()) address.append(", " + state);
		
		address.append(", " + country);
		
		if(!postalCode.isEmpty()) address.append(".Postal Code: " + postalCode);
		if(!phoneNumber.isEmpty()) address.append(".Phone Number: " + phoneNumber);
		
		return address.toString();
	}
}
