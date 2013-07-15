package com.pjcode.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.pjcode.domain.Order;
import com.pjcode.response.OrderDto;

public class OrderMapper {

	public static OrderDto map(Order order) {
			OrderDto dto = new OrderDto();
			dto.setId(order.getId());			
			dto.setPrice(order.getPrice());
			dto.setProduct(order.getProduct());
			dto.setQuantity(order.getQuantity());
			dto.setRecievedDate(order.getRecievedDate());
			dto.setStatus(order.getStatus());
			return dto;
	}
	
	public static List<OrderDto> map(Page<Order> orders) {
		List<OrderDto> dtos = new ArrayList<OrderDto>();
		for (Order order: orders) {
			dtos.add(map(order));
		}
		return dtos;
	}
}
