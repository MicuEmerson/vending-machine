package com.example.vendingMachine.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyProductResponse implements Serializable {
	private Long totalSpent;
	private String productName;
	private List<Long> change;
}
