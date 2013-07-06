package com.pjcode.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pjcode.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	
	Product findByName(String name);
	
	Page<Product> findByNameLike(String name, Pageable pageable);
	
}
