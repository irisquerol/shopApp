package com.app.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.shop.model.Cart;
import com.app.shop.model.Product;
import com.app.shop.services.CartService;
import com.app.shop.view.ResponseMessages;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping("/new")
	public ResponseEntity<String> createCart() {
		Cart cart = cartService.createCart();
		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseMessages.CART_CREATED + cart.getId());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cart> getCart(@PathVariable String id) {
		return ResponseEntity.ok(cartService.getCart(id));
	}

	@GetMapping("/list")
	public ResponseEntity<Map<String, Cart>> getCartList() {
		return ResponseEntity.ok(cartService.getList());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCart(@PathVariable String id) {
		cartService.deleteCart(id);
		return ResponseEntity.ok(id + ResponseMessages.CART_DELETED);
	}

	@DeleteMapping("/{cartId}/{productId}")
	public ResponseEntity<String> removeProduct(@PathVariable String cartId, @PathVariable int productId) {
		cartService.removeProduct(cartId, productId);
		return ResponseEntity.ok(productId + ResponseMessages.PRODUCT_REMOVED);

	}

	@PostMapping("/{id}/addProducts")
	public ResponseEntity<String> addProducts(@PathVariable String id, @RequestBody List<Product> products) {
		cartService.addProducts(id, products);
		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseMessages.PRODUCT_ADDED);

	}

}
