package com.example.vendingMachine.dtos.request;

import com.example.vendingMachine.validators.annotations.PriceValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest implements Serializable {
	private String productName;
	private @PriceValidator Long cost;
	private Long amountAvailable;
}
