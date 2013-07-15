package com.pjcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjcode.domain.Order;
import com.pjcode.repository.OrderRepository;

@Transactional
@Service
public class OrderService {

	@Autowired
	private OrderRepository repository;
	
	public Boolean create(Order order) {
		Order saved = repository.save(order);
		if (saved == null) 
			return false;
		
		return true;
	}
	
	public Boolean update(Order order) {
		Order existingOrder = repository.findOne(order.getId());
		if (existingOrder == null) 
			return false;
		
		existingOrder.setPrice(order.getPrice());
		existingOrder.setQuantity(order.getQuantity());
		existingOrder.setStatus(order.getStatus());
		
		Order saved = repository.save(existingOrder);
		if (saved == null) 
			return false;
		
		return true;
	}
	
	public Boolean delete(Order order) {
		Order existingOrder = repository.findOne(order.getId());
		if (existingOrder == null) 
			return false;
		
		repository.delete(existingOrder);
		Order deletedOrder = repository.findOne(order.getId());
		if (deletedOrder != null) 
			return false;
		
		return true;
	}
}
