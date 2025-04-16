package com.app.shop.services;

import java.time.Duration;
import java.time.LocalDateTime;
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

	private int cartCounter;

	/*
	 * Creates a new Cart
	 * 
	 * @return Cart
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
	 * Gets the Cart by its ID
	 * 
	 * @param id of the cart
	 * @return the info of the cart with the id
	 */
	public Cart getCart(String id) {
		Cart cart = cartStorage.get(id);
		if (cart == null) {
			throw new CartException(ResponseMessages.CART_NOT_FOUND);
		}
		return cart;

	}

	/**
	 * Gets all the carts with all their info
	 * 
	 * @return Map<String, Cart>
	 */
	public Map<String, Cart> getList() {
		if (cartStorage.isEmpty()) {
			throw new CartException(ResponseMessages.CART_LIST_EMPTY);
		}
		return cartStorage;
	}

	/**
	 * Removes the cart by its ID Uses {@link #getCart(String)} to validate the cart exists
	 * 
	 * @param id of the cart
	 * @throws CartException if validation fails
	 * @param id
	 */
	public void deleteCart(String id) {
		getCart(id);
		cartStorage.remove(id);
	}

	/**
	 * Adds a list of products to the indicated cart after validation
	 * 
	 * @param cartId
	 * @param products list (at least 1)
	 * @throws CartException if validation fails
	 */
	public void addProducts(String cartId, List<Product> products) {
		Cart cart = getCart(cartId);

		Set<Integer> newProductIds = cart.getProductIds();
		List<Product> newProducts = cart.getProducts();

		// Validation of each new register of product
		for (Product product : products) {
			if (newProductIds.contains(product.getId())) {
				throw new CartException(product.getId() + ResponseMessages.PRODUCT_ID_DUPLICATED);
			}

			if (product.getId() <= 0) {
				throw new CartException(product.getId() + ResponseMessages.PRODUCT_ID_INVALID);
			}

			if (product.getAmount() <= 0) {
				throw new CartException(product.getAmount() + ResponseMessages.PRODUCT_AMOUNT_INVALID);
			}
			newProductIds.add(product.getId());
			newProducts.add(product);

		}
		// After validation, adds all the products and ID to the cart and keeps the cart active
		cart.setProductIds(newProductIds);
		cart.setProducts(newProducts);
		cart.setStartTime(LocalDateTime.now());

	}

	/**
	 * Removes a product by its ID and cart ID
	 * 
	 * @param cartId
	 * @param productId
	 * @throws CartException if cart or product validation fails
	 */
	public void removeProduct(String cartId, int productId) {
		Cart cart = getCart(cartId);

		Product productToRemove = cart.getProducts().stream().filter(product -> product.getId() == productId)
				.findFirst().orElse(null);
		// validation product exist
		if (productToRemove == null) {
			throw new CartException(ResponseMessages.PRODUCT_NOT_FOUND);
		}

		// removes the product and keeps the cart active
		cart.getProductIds().remove(productId);
		cart.getProducts().remove(productToRemove);
		cart.setStartTime(LocalDateTime.now());
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
