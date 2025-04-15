package com.app.shop.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cart {

	private String id;
	private List<Product> products = new ArrayList<>();
	private LocalDateTime startTime;

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

}
