package com.pjcode.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name="product")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	// test commit from page
	@Column(unique=true)
	private String name;
	private Long itemsAvailable;
	private BigDecimal price;

	public Product() {}
	
	public Product(String name) {
		this.name = name;
	}
	
	public Product(String name, Long itemsAvailable, BigDecimal price) {
		this.name = name;
		this.itemsAvailable = itemsAvailable;
		this.price = price;
	}
		
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getItemsAvailable() {
		return itemsAvailable;
	}
	public void setItemsAvailable(Long itemsAvailable) {
		this.itemsAvailable = itemsAvailable;
	}	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", itemsAvailable="
				+ itemsAvailable + ", price=" + price + "]";
	}
}
