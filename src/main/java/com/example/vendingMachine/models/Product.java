package com.example.vendingMachine.models;

import com.example.vendingMachine.dtos.request.ProductRequest;
import com.example.vendingMachine.dtos.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	private Long sellerId;

	private String productName;

	private Long cost;

	private Long amountAvailable;

	public static Product fromProductRequestToProduct(ProductRequest productRequest){
		return Product.builder()
				.productName(productRequest.getProductName())
				.amountAvailable(productRequest.getAmountAvailable())
				.cost(productRequest.getCost())
				.build();
	}

	public static ProductResponse fromProductToProductResponse(Product product){
		return ProductResponse.builder()
				.id(product.getId())
				.productName(product.getProductName())
				.amountAvailable(product.getAmountAvailable())
				.cost(product.getCost())
				.build();
	}
}
