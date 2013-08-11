package com.pjcode.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.pjcode.domain.Order;
import com.pjcode.domain.OrderStatus;
import com.pjcode.domain.Product;
import com.pjcode.repository.OrderRepository;
import com.pjcode.repository.ProductRepository;
import com.pjcode.response.JqgridResponse;
import com.pjcode.response.StatusResponse;
import com.pjcode.response.OrderDto;
import com.pjcode.service.OrderService;
import com.pjcode.util.JqgridFilter;
import com.pjcode.util.JqgridObjectMapper;
import com.pjcode.util.OrderMapper;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo1/orders")
public class Demo1OrderController {
	
	private static final ThreadLocal<DateFormat> df
			= new ThreadLocal<DateFormat>(){
		@Override
		protected DateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMdd");
		}
	};
	
	protected static Logger logger = Logger.getLogger("controller");
	
	@Autowired
	private OrderRepository repository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired 
	private OrderService service;
	
	@RequestMapping
	public String getOrdersPage() {
		return "demo1/orders";
	}
	
	@RequestMapping(value="/records", produces="application/json")
	public @ResponseBody JqgridResponse<OrderDto> records(
    		@RequestParam("_search") Boolean search,
    		@RequestParam(value="filters", required=false) String filters,
    		@RequestParam(value="page", required=false) Integer page,
    		@RequestParam(value="rows", required=false) Integer rows,
    		@RequestParam(value="sidx", required=false) String sidx,
    		@RequestParam(value="sord", required=false) String sord) {

		Pageable pageRequest = new PageRequest(page-1, rows, new Sort(Direction.DESC, "recievedDate"));
		
		
		if (search == true) {
			return getFilteredRecords(filters, pageRequest);
			
		} 
			
		//Page<Order> orders = repository.findAll(pageRequest);
		Page<Order> orders = repository.findAll(pageRequest);
		
		JqgridResponse<OrderDto> response = new JqgridResponse<OrderDto>();
		if (orders != null) {
			List<OrderDto> orderDtos = OrderMapper.map(orders);
			response.setRows(orderDtos);
			response.setRecords(Long.valueOf(orders.getTotalElements()).toString());
			response.setTotal(Integer.valueOf(orders.getTotalPages()).toString());
			response.setPage(Integer.valueOf(orders.getNumber()+1).toString());
		}
		
		return response;
	}
	
	/**
	 * Helper method for returning filtered records
	 */
	public JqgridResponse<OrderDto> getFilteredRecords(String filters, Pageable pageRequest) {
		
		BigDecimal qPrice = null;
		String qProductName = null;
		Long qQuantity = null;
		Date qRecievedDate = null;
		Integer qStatus = null;
		
		JqgridFilter jqgridFilter = JqgridObjectMapper.map(filters);
		for (JqgridFilter.Rule rule: jqgridFilter.getRules()) {
			if (rule.getField().equals("price"))
				qPrice = new BigDecimal(rule.getData());
			else if (rule.getField().equals("productName"))
				qProductName = rule.getData();
			else if (rule.getField().equals("quantity"))
				qQuantity = Long.getLong(rule.getData());
			else if (rule.getField().equals("receivedDate"))
				try {
					qRecievedDate = df.get().parse(rule.getData());
				} catch(Exception ex) {
					logger.error(ex);
				}
			else if (rule.getField().equals("status"))
				qStatus = Integer.getInteger(rule.getData());
		}
		
		Page<Order> orders = null;
		if (qPrice != null)
			orders = repository.findByPrice(qPrice, pageRequest);
		else if (qProductName != null)
			// get the ProductByProductName, then get the Order by product
			;
		else if (qQuantity != null)
			orders = repository.findByQuantity(qQuantity, pageRequest);
		else if (qRecievedDate != null)
			orders = repository.findByRecievedDate(qRecievedDate, pageRequest);
		else if (qStatus != null)
			orders = repository.findByStatus(qStatus, pageRequest);
		
		JqgridResponse<OrderDto> response = new JqgridResponse<OrderDto>();
		
		if (orders != null) {
			List<OrderDto> orderDtos = OrderMapper.map(orders);
			response.setRows(orderDtos);
			response.setRecords(Long.valueOf(orders.getTotalElements()).toString());
			response.setTotal(Integer.valueOf(orders.getTotalPages()).toString());
			response.setPage(Integer.valueOf(orders.getNumber()+1).toString());
		}
		return response;
	}
	
	@RequestMapping(value="/get", produces="application/json")
	public @ResponseBody OrderDto get(@RequestBody OrderDto order) {
		return OrderMapper.map(repository.findOne(order.getId()));
	}

	@RequestMapping(value="/create", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse create(
			@RequestParam(value = "product.id") Long productId,
			@RequestParam BigDecimal price,
			@RequestParam Long quantity) {
		
		//find the Product first
		Product product = productRepository.findOne(productId);
		if (product != null) {
			Order newOrder = new Order();
			newOrder.setPrice(price);
			newOrder.setProduct(product);
			newOrder.setQuantity(quantity);
			newOrder.setRecievedDate(new Date());
			newOrder.setStatus(OrderStatus.NEW.getCode());
			newOrder.setComment("");
			Boolean result = service.create(newOrder);
			return new StatusResponse(result);
		} else {
			return new StatusResponse(false, "Cannot find product with id: "+ productId);
		}
	}
	
	@RequestMapping(value="/update", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse update(
			@RequestParam Long id,
			@RequestParam BigDecimal price,
			@RequestParam Long quantity,
			@RequestParam Integer status) {
		
		Order existingOrder = new Order();
		existingOrder.setId(id);
		existingOrder.setPrice(price);
		existingOrder.setQuantity(quantity);
		existingOrder.setPrice(price);
		existingOrder.setStatus(status);
		Boolean result = service.update(existingOrder);
		return new StatusResponse(result);
	}
	
	@RequestMapping(value="/delete", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse delete(
			@RequestParam Long id) {

		Order existingOrder = new Order();
		existingOrder.setId(id);
		Boolean result = service.delete(existingOrder);
		return new StatusResponse(result);
	}
}
