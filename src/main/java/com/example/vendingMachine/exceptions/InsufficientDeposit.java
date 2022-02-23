package com.example.vendingMachine.exceptions;

public class InsufficientDeposit extends RuntimeException {
	private String message;

	public InsufficientDeposit() {}
	public InsufficientDeposit(String message) {
		super(message);
	}
}