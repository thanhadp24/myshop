package com.shopapp.common.entity;

import java.util.Date;
import java.util.Objects;

import com.shopapp.common.entity.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "reviews")
public class Review extends IdBaseEntity {

	@Column(length = 128, nullable = false)
	private String headline;

	@Column(length = 500, nullable = false)
	private String comment;

	private int rating;
	private int votes;

	@Column(nullable = false)
	private Date reviewTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@Transient
	private boolean upvotedByCurrentCustomer;
	
	@Transient
	private boolean downVotedByCurrentCustomer;
	
	public Review() {
	}
	
	public Review(int id) {
		this.id = id;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	public boolean isUpvotedByCurrentCustomer() {
		return upvotedByCurrentCustomer;
	}

	public void setUpvotedByCurrentCustomer(boolean upvotedByCurrentCustomer) {
		this.upvotedByCurrentCustomer = upvotedByCurrentCustomer;
	}

	public boolean isDownVotedByCurrentCustomer() {
		return downVotedByCurrentCustomer;
	}

	public void setDownVotedByCurrentCustomer(boolean downVotedByCurrentCustomer) {
		this.downVotedByCurrentCustomer = downVotedByCurrentCustomer;
	}

	@Override
	public String toString() {
		return "Review [headline=" + headline + ", comment=" + comment + ", rating=" + rating + ", reviewTime="
				+ reviewTime + ", product=" + product.getShortName() + ", customer=" + customer.getFullName() + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) {
			return true;
		}
		
		if(!(obj instanceof Review that)) {
			return false;
		}
		
		return this.getId() == that.getId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getId());
	}
}
