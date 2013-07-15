package com.pjcode.controller;

import java.math.BigDecimal;
import java.util.List;

import com.pjcode.domain.Product;
import com.pjcode.repository.ProductRepository;
import com.pjcode.response.JqgridResponse;
import com.pjcode.response.StatusResponse;
import com.pjcode.response.ProductDto;
import com.pjcode.service.ProductService;
import com.pjcode.util.JqgridFilter;
import com.pjcode.util.JqgridObjectMapper;
import com.pjcode.util.ProductMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/products")
public class ProductController {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired 
	private ProductService service;
	
	@RequestMapping
	public String getProductsPage() {
		return "products";
	}
	
	@RequestMapping(value="/records", produces="application/json")
	public @ResponseBody JqgridResponse<ProductDto> records(
    		@RequestParam("_search") Boolean search,
    		@RequestParam(value="filters", required=false) String filters,
    		@RequestParam(value="page", required=false) Integer page,
    		@RequestParam(value="rows", required=false) Integer rows,
    		@RequestParam(value="sidx", required=false) String sidx,
    		@RequestParam(value="sord", required=false) String sord) {

		Pageable pageRequest = new PageRequest(page-1, rows);
		
		if (search == true) {
			return getFilteredRecords(filters, pageRequest);
			
		} 
			
		Page<Product> products = repository.findAll(pageRequest);
		
		JqgridResponse<ProductDto> response = new JqgridResponse<ProductDto>();
		if (products != null) {
			List<ProductDto> productDtos = ProductMapper.map(products);
			response.setRows(productDtos);
			response.setRecords(Long.valueOf(products.getTotalElements()).toString());
			response.setTotal(Integer.valueOf(products.getTotalPages()).toString());
			response.setPage(Integer.valueOf(products.getNumber()+1).toString());
		}
		
		return response;
	}
	
	/**
	 * Helper method for returning filtered records
	 */
	public JqgridResponse<ProductDto> getFilteredRecords(String filters, Pageable pageRequest) {
		String qName = null;
		BigDecimal qPrice = null;
		
		JqgridFilter jqgridFilter = JqgridObjectMapper.map(filters);
		for (JqgridFilter.Rule rule: jqgridFilter.getRules()) {
			if (rule.getField().equals("name"))
				qName = rule.getData();
			else if (rule.getField().equals("price"))
				qPrice = new BigDecimal(rule.getData());
		}
		
		Page<Product> products = null;
		if (qName != null) 
			products = repository.findByNameLike("%"+qName+"%", pageRequest);
		else if (qPrice != null)
			products = repository.findByPrice(qPrice, pageRequest);
		
		JqgridResponse<ProductDto> response = new JqgridResponse<ProductDto>();
		
		if (products != null) {
			List<ProductDto> productDtos = ProductMapper.map(products);
			response.setRows(productDtos);
			response.setRecords(Long.valueOf(products.getTotalElements()).toString());
			response.setTotal(Integer.valueOf(products.getTotalPages()).toString());
			response.setPage(Integer.valueOf(products.getNumber()+1).toString());
		}
		return response;
	}
	
	@RequestMapping(value="/get", produces="application/json")
	public @ResponseBody ProductDto get(@RequestBody ProductDto product) {
		return ProductMapper.map(repository.findByName(product.getName()));
	}

	@RequestMapping(value="/create", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse create(
			@RequestParam String name,
			@RequestParam Long itemsAvailable,
			@RequestParam BigDecimal price) {
		
		Product newProduct = new Product(name, itemsAvailable, price);
		Boolean result = service.create(newProduct);
		return new StatusResponse(result);
	}
	
	@RequestMapping(value="/update", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse update(
			@RequestParam String name,
			@RequestParam Long itemsAvailable,
			@RequestParam BigDecimal price) {
		
		Product existingProduct = new Product(name, itemsAvailable, price);
		Boolean result = service.update(existingProduct);
		return new StatusResponse(result);
	}
	
	@RequestMapping(value="/delete", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse delete(
			@RequestParam String name) {

		Product existingProduct = new Product(name);
		Boolean result = service.delete(existingProduct);
		return new StatusResponse(result);
	}
}
