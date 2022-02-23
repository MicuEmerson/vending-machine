package com.example.vendingMachine.models;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Coin {
	FIVE(5L),
	TEN(10L),
	TWENTY(20L),
	FIFTY(50L),
	HUNDRED(100L);

	private Long value;

	Coin(Long value){
		this.value = value;
	}

	@JsonValue
	public Long getValue() {
		return value;
	}
}
