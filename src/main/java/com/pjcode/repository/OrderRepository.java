package com.pjcode.repository;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pjcode.domain.Order;
import com.pjcode.domain.Product;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	Page<Order> findByPrice(BigDecimal price, Pageable pageable);
	
	Page<Order> findByProduct(Product product, Pageable pageable);
	
	Page<Order> findByQuantity(Long quantity, Pageable pageable);
	
	Page<Order> findByRecievedDate(Date recievedDate, Pageable pageable);
	
	Page<Order> findByStatus(Integer status, Pageable pageable);
	
	List<Order> findByStatus(Integer status);
	
}
