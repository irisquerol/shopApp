package com.app.shop.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.app.shop.model.Cart;
import com.app.shop.model.Product;

@Service
public class CartService {

	private final Map<String, Cart> cartStorage = new LinkedHashMap<>();
	private int cartCounter;

	/*
	 * createCart
	 * 
	 * @return new Cart
	 */
	public Cart createCart() {
		if (cartStorage.isEmpty()) {
			cartCounter = 1;
		}
		String id = String.format("CART%03d", cartCounter++);

		Cart cart = new Cart(id);
		cartStorage.put(id, cart);
		return cart;
	}

	/**
	 * getCart
	 * 
	 * @param id
	 * @return the cart with the id
	 */
	public Cart getCart(String id) {
		return cartStorage.get(id);
	}

	public Map<String, Cart> getList() {
		return cartStorage;
	}

	public void addProducts(String id, List<Product> products) {
		Cart cart = cartStorage.get(id);
		if (cart != null) {
			cart.getProducts().addAll(products);
		}
	}

	public void deleteCart(String id) {
		cartStorage.remove(id);
	}

	/**
	 * Thread called every 60 seconds Deletes the cart entry if it has more than 10
	 * minutes
	 */
	@Scheduled(fixedRate = 60000)
	public void removeInactiveCarts() {
		LocalDateTime now = LocalDateTime.now();
		cartStorage.entrySet()
				.removeIf(entry -> Duration.between(entry.getValue().getStartTime(), now).toMinutes() >= 10);
	}
}
