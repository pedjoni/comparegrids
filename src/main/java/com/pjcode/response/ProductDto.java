package com.pjcode.response;

import java.io.Serializable;

public class ProductDto implements Serializable {
	
	private static final long serialVersionUID = 9087857787928500650L;

	private Long id;
	private String name;
	private Long itemsAvailable;
	
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
		return "ProductDto [id=" + id + ", name=" + name + ", itemsAvailable="
				+ itemsAvailable + "]";
	}
}
