package com.app.shop.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.app.shop.exceptions.CartException;
import com.app.shop.model.Cart;
import com.app.shop.model.Product;
import com.app.shop.view.ResponseMessages;

class CartServiceTest {
	private CartService cartService;

	@BeforeEach
	void setUp() {
		cartService = new CartService();
	}

	@Test
	void testCreateCart() {
		Cart cart = cartService.createCart();
		assertNotNull(cart);

	}

	@Test
	void testGetCart() {
		Cart cart = cartService.createCart();
		cart = cartService.getCart(cart.getId());
		assertNotNull(cart);

	}

	@Test
	void testGetCartNOK() {
		CartException ex = assertThrows(CartException.class, () -> {
			cartService.getCart("CART001");
		});
		assertEquals(ResponseMessages.CART_NOT_FOUND, ex.getMessage());
	}

	@Test
	void testGetList() {
		cartService.createCart();
		Map<String, Cart> cartStorage = cartService.getList();
		assertNotNull(cartStorage);
		assertFalse(cartStorage.isEmpty());
	}

	@Test
	void testGetListNOK() {
		CartException ex = assertThrows(CartException.class, () -> {
			cartService.getList();
		});
		assertEquals(ResponseMessages.CART_LIST_EMPTY, ex.getMessage());
	}

	@Test
	void testDeleteCart() {
		Cart cart = cartService.createCart();
		cartService.deleteCart(cart.getId());
		assertThrows(CartException.class, () -> cartService.getCart(cart.getId()));

	}

	@Test
	void testDeleteCartNOK() {
		CartException ex = assertThrows(CartException.class, () -> {
			cartService.deleteCart(Mockito.any());
		});
		assertEquals(ResponseMessages.CART_NOT_FOUND, ex.getMessage());

	}

	@Test
	void testAddProducts() {
		Cart cart = cartService.createCart();

		cartService.addProducts(cart.getId(), getProducts());
		assertEquals(getProducts().size(), cart.getProducts().size());

	}

	@Test
	void testAddProductsNOK_DUPLICATED() {
		Cart cart = cartService.createCart();
		List<Product> products = getProducts();
		products.add(new Product(1, "Ball", 3));

		CartException ex = assertThrows(CartException.class, () -> {
			cartService.addProducts(cart.getId(), products);
		});
		assertEquals(1 + ResponseMessages.PRODUCT_ID_DUPLICATED, ex.getMessage());

	}

	@Test
	void testAddProductsNOK_INVALID_ID() {
		Cart cart = cartService.createCart();
		List<Product> products = new ArrayList<>(List.of(new Product(-1, "Ball", 3)));

		CartException ex = assertThrows(CartException.class, () -> {
			cartService.addProducts(cart.getId(), products);
		});
		assertEquals(-1 + ResponseMessages.PRODUCT_ID_INVALID, ex.getMessage());

	}

	@Test
	void testAddProductsNOK_INVALID_AMOUNT() {
		Cart cart = cartService.createCart();
		List<Product> products = new ArrayList<>(List.of(new Product(1, "Ball", -3)));

		CartException ex = assertThrows(CartException.class, () -> {
			cartService.addProducts(cart.getId(), products);
		});
		assertEquals(-3 + ResponseMessages.PRODUCT_AMOUNT_INVALID, ex.getMessage());

	}

	@Test
	void testRemoveProduct() {
		Cart cart = cartService.createCart();
		List<Product> products = getProducts();

		cartService.addProducts(cart.getId(), products);

		cartService.removeProduct(cart.getId(), getProducts().get(1).getId());
		assertNotEquals(products.size(), cart.getProducts().size());
		assertNotEquals(products.size(), cart.getProductIds().size());
		assertEquals(cart.getProductIds().size(), cart.getProducts().size());

	}

	@Test
	void testRemoveProductNOK() {
		Cart cart = cartService.createCart();

		cartService.addProducts(cart.getId(), getProducts());

		CartException ex = assertThrows(CartException.class, () -> {
			cartService.removeProduct(cart.getId(), -3);
		});
		assertEquals(ResponseMessages.PRODUCT_NOT_FOUND, ex.getMessage());

	}

	private List<Product> getProducts() {

		Product product1 = new Product(1, "Ball", 3);
		Product product2 = new Product(2, "Skate", 1);

		return new ArrayList<>(List.of(product1, product2));
	}

}
