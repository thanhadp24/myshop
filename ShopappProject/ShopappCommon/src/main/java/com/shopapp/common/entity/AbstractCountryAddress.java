package com.shopapp.common.entity;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractCountryAddress extends AbstractAddress{

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	protected Country country;
	
	public Country getCountry() {
		return country;
	}
	
	public void setCountry(Country country) {
		this.country = country;
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
