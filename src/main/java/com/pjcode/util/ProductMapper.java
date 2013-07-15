package com.pjcode.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.pjcode.domain.Product;
import com.pjcode.response.ProductDto;

public class ProductMapper {

	public static ProductDto map(Product product) {
			ProductDto dto = new ProductDto();
			dto.setId(product.getId());
			dto.setName(product.getName());
			dto.setItemsAvailable(product.getItemsAvailable());
			dto.setPrice(product.getPrice());
			return dto;
	}
	
	public static List<ProductDto> map(Page<Product> products) {
		List<ProductDto> dtos = new ArrayList<ProductDto>();
		for (Product product: products) {
			dtos.add(map(product));
		}
		return dtos;
	}
}
