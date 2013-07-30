package com.pjcode.service;

import java.util.Date;
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
	
	public static final long PROCESSING_DELAY = 10*60*1000; //in milliseconds 
	
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
		order.setComment("");
		
		Order saved = repository.save(existingOrder);
		if (saved == null) 
			return false;
		
		return true;
	}
	
	
	/**
	 * Check all the Orders with the status NEW and if they are older then predefined
	 * time (PROCESSING_DELAY) do the following:
	 * - if order is for the product that is available change the status to PROCESSED
	 *   and decrease number of products available
	 * - if the order is for the product that either does not exists or quantity
	 *   of the available product less than the one requested in the order, 
	 *   change the status to ERRORED
	 * This method is called periodically
	 * 
	 */
	@Scheduled(fixedDelay=30000)
	public void process() {
		
		List<Order> orders = repository.findByStatus(OrderStatus.NEW.getCode());
		logger.debug(orders.size() + " orders with the status NEW retrieved");
		
		Date timeNow = new Date();
		
		for (Order order : orders) {
			if (timeNow.getTime() - order.getRecievedDate().getTime() > PROCESSING_DELAY) {
				if (order.getQuantity() < order.getProduct().getItemsAvailable()) {
					order.getProduct().setItemsAvailable(order.getProduct().getItemsAvailable() - order.getQuantity());
					order.setStatus(OrderStatus.PROCESSED.getCode());
					logger.debug("Status of the Order with the ID: " + order.getId() + " changed to PROCESSED");
					order.setComment("");
				} else {
					order.setStatus(OrderStatus.ERRORED.getCode());
					order.setComment("ERRORED: Not enough product available");
					logger.error("Order received for product that has not enought items available. Order: " + order);
				}
			}
		}
		
	}
}
