package com.example.vendingMachine.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyProductRequest implements Serializable {
	private Long productId;
	private Long amountOfProducts;
}
