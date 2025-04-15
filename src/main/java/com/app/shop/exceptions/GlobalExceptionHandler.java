package com.app.shop.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CartException.class)
	public ResponseEntity<?> handleCartException(CartException ex) {
		return ResponseEntity.status(451).body(ex.getMessage());

	}
}
