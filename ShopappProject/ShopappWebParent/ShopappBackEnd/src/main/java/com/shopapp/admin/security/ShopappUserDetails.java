package com.shopapp.admin.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.shopapp.common.entity.User;

public class ShopappUserDetails implements UserDetails{

	private static final long serialVersionUID = 1L;
	private User user;
	
	public ShopappUserDetails(User user) {
		this.user = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return user.getRoles()
			.stream()
			.map(role -> new SimpleGrantedAuthority(role.getName()))
			.toList();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return user.isEnabled();
	}
	
	public void setFirstName(String firstName) {
		user.setFirstName(firstName);
	}
	
	public void setLastName(String lastName) {
		user.setLastName(lastName);
	}
	
	public String getFullname() {
		return user.getFirstName() + " " + user.getLastName();
	}
	
	public boolean hasRole(String roleName) {
		return user.hasRole(roleName);
	}
	
}
