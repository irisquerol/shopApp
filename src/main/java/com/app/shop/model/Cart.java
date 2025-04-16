package com.app.shop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cart {

	private String id;
	private List<Product> products = new ArrayList<>();
	private LocalDateTime startTime;
	private Set<Integer> productIds = new HashSet<>();


	public Cart(String id) {
		this.id = id;
		this.startTime = LocalDateTime.now();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public Set<Integer> getProductIds() {
		return productIds;
	}

	public void setProductIds(Set<Integer> productIds) {
		this.productIds = productIds;
	}
	

}
