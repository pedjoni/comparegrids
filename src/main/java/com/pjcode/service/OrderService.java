package com.pjcode.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import com.pjcode.domain.Order;
import com.pjcode.domain.OrderStatus;
import com.pjcode.repository.OrderRepository;

@Transactional
@Service
public class OrderService {
	
	protected static Logger logger = Logger.getLogger("service");

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
	
	public Boolean cancel(Order order) {
		Order existingOrder = repository.findOne(order.getId());
		if (existingOrder == null) 
			return false;
		
		existingOrder.setStatus(OrderStatus.CANCELLED.getCode());
		
		Order saved = repository.save(existingOrder);
		if (saved == null) 
			return false;
		
		return true;
	}
	
	
	/**
	 * Check all the Orders with the status NEW and if they are older then predefined
	 * time (10 minutes) do the following:
	 * - if order is for the product that is available change the status to PROCESSED
	 * - if the order is for the product that either does not exits or quantity
	 *   of the available product is not enough, change the status to ERRORED
	 * This method is called periodically (every 30 sec)
	 * 
	 */
	@Scheduled(fixedDelay=30000)
	public void process() {
		
		List<Order> orders = repository.findByStatus(OrderStatus.NEW.getCode());
		logger.debug(orders.size() + " orders with the status NEW retrieved");
		
		
	}
}
