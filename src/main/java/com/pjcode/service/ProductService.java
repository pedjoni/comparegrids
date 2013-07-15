package com.pjcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pjcode.domain.Product;
import com.pjcode.repository.ProductRepository;

@Transactional
@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	public Boolean create(Product product) {
		Product saved = repository.save(product);
		if (saved == null) 
			return false;
		
		return true;
	}
	
	public Boolean update(Product product) {
		Product existingProduct = repository.findByName(product.getName());
		if (existingProduct == null) 
			return false;
		
		existingProduct.setItemsAvailable(product.getItemsAvailable());
		existingProduct.setPrice(product.getPrice());
		
		Product saved = repository.save(existingProduct);
		if (saved == null) 
			return false;
		
		return true;
	}
	
	public Boolean delete(Product product) {
		Product existingProduct = repository.findByName(product.getName());
		if (existingProduct == null) 
			return false;
		
		repository.delete(existingProduct);
		Product deletedProduct = repository.findByName(product.getName());
		if (deletedProduct != null) 
			return false;
		
		return true;
	}
}
