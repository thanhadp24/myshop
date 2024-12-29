package com.shopapp.common.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name",length = 40, nullable = false, unique = true)
	private String name;
	
	@Column(name = "description",length = 150, nullable = false)
	private String description;
	
	public Role() {
	}
	
	public Role(Integer id) {
		this.id = id;
	}
	
	public Role(String name) {
		this.name = name;
	}
	
	public Role(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Role)) {
			return false;
		}
		
		Role that = (Role) o;
		
		return that.getId() == this.getId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}

	@Override
	public String toString() {
		return this.name;
	}
	
	
}
