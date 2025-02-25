package com.shopapp.common.entity;

import java.beans.Transient;
import java.util.Date;

import com.shopapp.common.enumm.AuthenticationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends AbstractCountryAddress{

	@Column(length = 45, unique = true, nullable = false)
	private String email;

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "created_at")
	private Date createdAt;

	private boolean enabled;

	@Column(name = "verification_code", length = 64)
	private String verificationCode;

	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type", length = 10)
	private AuthenticationType type;
	
	@Column(name = "reset_password_token", length = 30)
	private String resetPasswordToken;
	
	public Customer() {
	}
	
	public Customer(Integer id) {
		this.id = id;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}
	
	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public AuthenticationType getType() {
		return type;
	}

	public void setType(AuthenticationType type) {
		this.type = type;
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
}
