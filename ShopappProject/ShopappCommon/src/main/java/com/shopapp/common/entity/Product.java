
package com.shopapp.common.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "products")
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, unique = true, length = 256)
	private String name;

	@Column(nullable = false, unique = true, length = 256)
	private String alias;

	@Column(name = "short_description", nullable = false, length = 512)
	private String shortDescription;

	@Column(name = "full_description", nullable = false, length = 4096)
	private String fullDescription;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;

	private boolean enabled;

	@Column(name = "in_stock")
	private boolean inStock;

	private float cost;

	private float price;

	@Column(name = "discount_percent")
	private float discountPercent;

	private float length;
	private float width;
	private float height;
	private float weight;

	@Column(name = "main_image", nullable = false)
	private String mainImage;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", referencedColumnName = "id")
	private Brand brand;

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductImage> images = new HashSet<>();
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductDetail> productDetails = new ArrayList<>();
	
	public Product() {
	}
	
	public Product(Integer id) {
		this.id = id;
	}

	public List<ProductDetail> getProductDetails() {
		return productDetails;
	}

	public void setProductDetails(List<ProductDetail> productDetails) {
		this.productDetails = productDetails;
	}
	
	public Set<ProductImage> getImages() {
		return images;
	}

	public void setImages(Set<ProductImage> images) {
		this.images = images;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
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

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getFullDescription() {
		return fullDescription;
	}

	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isInStock() {
		return inStock;
	}

	public void setInStock(boolean inStock) {
		this.inStock = inStock;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getDiscountPercent() {
		return discountPercent;
	}

	public void setDiscountPercent(float discountPercent) {
		this.discountPercent = discountPercent;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Brand getBrand() {
		return brand;
	}

	public void setBrand(Brand brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + "]";
	}
	
	public void addExtraImage(String imageName) {
		this.images.add(new ProductImage(imageName, this));
	}
	

	public void addExtraDetail(Integer id, String name, String value) {
		this.productDetails.add(new ProductDetail(id, name, value, this));
	}
	
	public void addExtraDetail(String name, String value) {
		this.productDetails.add(new ProductDetail(name, value, this));
	}
	
	@Transient
	public String getMainImagePath() {
		if(this.id == null || this.getMainImage() == null) {
			return "/images/image-thumbnail.png";
		}
		return "/product-images/" + this.id + "/" + this.mainImage;
	}

	public boolean containsImageFileName(String fileName) {
		return this.images.stream().anyMatch(i -> i.getName().equals(fileName));
	}
	
	@Transient
	public String getShortName() {
		if(this.name.length() > 70) {
			return this.name.substring(0, 70).concat("...");
		}
		return this.name;
	}
	
	
	@Transient
	public float getDiscountPrice() {
		if(this.discountPercent > 0) {
			return this.price*(1 - this.discountPercent/100);
		}
		return this.price;
	}
	
}
