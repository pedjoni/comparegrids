package com.pjcode.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.pjcode.domain.Product;

public class OrderDto implements Serializable {
	
	private static final long serialVersionUID = 9087857787928500650L;

	private Long id;
	private BigDecimal price;
	private Long quantity;
	private Integer status;
	private Date recievedDate;
	private Product product;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getRecievedDate() {
		return recievedDate;
	}
	public void setRecievedDate(Date recievedDate) {
		this.recievedDate = recievedDate;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Override
	public String toString() {
		return "OrderDto [id=" + id + ", price=" + price + ", quantity="
				+ quantity + ", status=" + status + ", recievedDate="
				+ recievedDate + ", product=" + product + "]";
	}
}
