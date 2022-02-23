package com.example.vendingMachine.exceptions;

public class ActionNotPermitted extends RuntimeException{
	private String message;

	public ActionNotPermitted(){}
	public ActionNotPermitted(String message) {
		super(message);
	}
}
