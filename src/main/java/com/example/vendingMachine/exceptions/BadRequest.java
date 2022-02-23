package com.example.vendingMachine.exceptions;

public class BadRequest extends RuntimeException {
	private String message;

	public BadRequest() {}
	public BadRequest(String message) {
		super(message);
	}
}
