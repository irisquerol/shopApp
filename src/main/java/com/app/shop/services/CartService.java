package com.app.shop.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.app.shop.exceptions.CartException;
import com.app.shop.model.Cart;
import com.app.shop.model.Product;
import com.app.shop.view.ResponseMessages;

@Service
public class CartService {

	private final Map<String, Cart> cartStorage = new LinkedHashMap<>();
	private final Set<Integer> productIds = new HashSet<>();

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
		Cart cart = cartStorage.get(id);
		if (cart == null) {
			throw new CartException(ResponseMessages.CART_NOT_FOUND);
		}
		return cart;

	}

	/**
	 * @return
	 */
	public Map<String, Cart> getList() {
		return cartStorage;
	}

	/**
	 * @param id
	 */
	public void deleteCart(String id) {
		Cart cart = getCart(id);
		if (cart == null) {
			throw new CartException(ResponseMessages.CART_NOT_FOUND);
		}
		cartStorage.remove(id);
	}

	/**
	 * @param id
	 * @param products
	 */
	public void addProducts(String cartId, List<Product> products) {
		Cart cart = getCart(cartId);
		if (cart == null) {
			throw new CartException(ResponseMessages.CART_NOT_FOUND);
		}

		for (Product product : products) {
			if (productIds.contains(product.getId())) {
				throw new CartException(ResponseMessages.PRODUCT_ID_DUPLICATED + product.getId());
			}

			if (product.getId() <= 0) {
				throw new CartException(ResponseMessages.PRODUCT_ID_INVALID);
			}

			if (product.getAmount() <= 0) {
				throw new CartException(ResponseMessages.PRODUCT_AMOUNT_INVALID);
			}
			productIds.add(product.getId());
			cart.getProducts().add(product);
		}

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
