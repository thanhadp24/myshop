package com.shopapp.common.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "addresses")
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	
	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;
	
	@Column(name = "phone_number", length = 15, nullable = false)
	private String phoneNumber;
	
	
	@Column(name = "address_line_1", length = 64, nullable = false)
	private String addressLine1;

	@Column(name = "address_line_2", length = 64)
	private String addressLine2;
	
	@Column(length = 45, nullable = false)
	private String city;
	
	@Column(length = 45, nullable = false)
	private String state;
	
	@Column( name = "postal_code", length = 10, nullable = false)
	private String postalCode;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private Country country;
	
	@Column(name = "default_address")
	private boolean defaultForShipping;

	public Address() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public boolean isDefaultForShipping() {
		return defaultForShipping;
	}

	public void setDefaultForShipping(boolean defaultForShipping) {
		this.defaultForShipping = defaultForShipping;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		
		if(!(obj instanceof Address that)) {
			return false;
		}
		
		return that.getId() == getId();
	}

	@Override
	public String toString() {
		StringBuilder address = new StringBuilder(firstName);
		 
		if(lastName != null && !lastName.isEmpty()) address.append(" " + lastName);
		
		if(!addressLine1.isEmpty()) address.append(".Address: " + addressLine1);
		
		if(addressLine2 != null && !addressLine2.isEmpty()) address.append(" - " + addressLine2);
		
		if(!city.isEmpty()) address.append(", " + city);
		
		if(state != null && !state.isEmpty()) address.append(", " + state);
		
		address.append(", " + country.getName());
		
		if(!postalCode.isEmpty()) address.append(".Postal Code: " + postalCode);
		if(!phoneNumber.isEmpty()) address.append(".Phone Number: " + phoneNumber);
		
		return address.toString();
	}
	
	
}
