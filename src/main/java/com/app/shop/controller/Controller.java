package com.app.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shop.exceptions.CartException;
import com.app.shop.model.Cart;
import com.app.shop.model.Product;
import com.app.shop.services.CartService;
import com.app.shop.view.ResponseMessages;

@RestController
@RequestMapping("/cart")
public class Controller {

	@Autowired
	private CartService cartService;

	@PostMapping("/new")
	public ResponseEntity<String> createCart() {
		Cart cart = cartService.createCart();
		if (cart == null) {
			throw new CartException(ResponseMessages.CART_NOT_CREATED);
		}
		return ResponseEntity.ok(ResponseMessages.CART_CREATED + cart.getId());
	}

	@GetMapping("/{id}")
	public Cart getCart(@PathVariable String id) {
		Cart cart = cartService.getCart(id);
		if (cart == null) {
			throw new CartException(ResponseMessages.CART_NOT_FOUND);
		}
		return cart;
	}

	@GetMapping("/list")
	public Map<String, Cart> getCartList() {
		return cartService.getList();
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCart(@PathVariable String id) {
		Cart cart = cartService.getCart(id);
		if (cart == null) {
			throw new CartException(ResponseMessages.CART_NOT_FOUND);
		}
		cartService.deleteCart(id);
		return ResponseEntity.ok(id + ResponseMessages.CART_DELETED);
	}

	@PostMapping("/{id}/addProduct")
	public ResponseEntity<?> addProducts(@PathVariable String id, @RequestBody List<Product> products) {
		cartService.addProducts(id, products);
		return ResponseEntity.ok().build();
	}

}
