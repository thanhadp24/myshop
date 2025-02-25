package com.shopapp.common.entity.order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.shopapp.common.entity.IdBaseEntity;
import com.shopapp.common.enumm.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "order_track")
public class OrderTrack extends IdBaseEntity{
	
	@Enumerated(EnumType.STRING)
	@Column(length = 45, nullable = false)
	private OrderStatus status;
	
	@Column(name = "updated_time")
	private Date updatedTime;
	
	@Column(length = 256)
	private String notes;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;
	
	public OrderTrack() {
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@Transient
	public String getUpdatedTimeOnForm() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		return df.format(this.updatedTime);
	}
	
	public void setUpdatedTimeOnForm(String dateString) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		
		try {
			this.updatedTime = df.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
}
