package com.example.vendingMachine.exceptions;

public class OutOfStock extends RuntimeException {
	private String message;

	public OutOfStock() {}
	public OutOfStock(String message) {
		super(message);
	}
}
