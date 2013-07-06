package com.pjcode.domain;

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
	
	@Column(unique=true)
	private String name;
	private Long itemsAvailable;

	public Product() {}
	
	public Product(String name) {
		this.name = name;
	}
	
	public Product(String name, Long itemsAvailable) {
		this.name = name;
		this.itemsAvailable = itemsAvailable;
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

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", itemsAvailable="
				+ itemsAvailable + "]";
	}

}
