package com.shopapp.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "states")
public class State extends IdBaseEntity{

	@Column(nullable = false, length = 100)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", referencedColumnName = "id")
	private Country country;

	public State() {
	}
	
	public State(String name, Country country) {
		this.name = name;
		this.country = country;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
