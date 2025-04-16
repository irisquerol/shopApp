package com.app.shop.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.app.shop.exceptions.CartException;
import com.app.shop.model.Cart;
import com.app.shop.model.Product;
import com.app.shop.services.CartService;
import com.app.shop.view.ResponseMessages;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
	@InjectMocks
	private Controller controller;

	@Mock
	private CartService cartService;

	@Test
	void testCreateCart() {
		when(cartService.createCart()).thenReturn(new Cart("CART001"));
		ResponseEntity<String> response = controller.createCart();
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

	}

	@Test
	void testGetCart() {
		when(cartService.getCart(Mockito.any())).thenReturn(new Cart("CART001"));

		ResponseEntity<Cart> response = controller.getCart(Mockito.any());

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("CART001", response.getBody().getId());
	}

	@Test
	void testGetCartList() {
		Map<String, Cart> mockMap = Map.of("CART001", new Cart("CART001"));
		when(cartService.getList()).thenReturn(mockMap);

		ResponseEntity<Map<String, Cart>> response = controller.getCartList();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
	}

	@Test
	void testDeleteCart() {
		doNothing().when(cartService).deleteCart(Mockito.any());

		ResponseEntity<String> response = controller.deleteCart(Mockito.any());

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testRemoveProduct() {
		doNothing().when(cartService).removeProduct("CART001", 1);

		ResponseEntity<String> response = controller.removeProduct("CART001", 1);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("1"));

	}

	@Test
	void testAddProducts() {
		List<Product> products = List.of(new Product(1, "Ball", 3));
		doNothing().when(cartService).addProducts("CART001", products);

		ResponseEntity<String> response = controller.addProducts("CART001", products);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(ResponseMessages.PRODUCT_ADDED, response.getBody());

	}

}
