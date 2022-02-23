package com.example.vendingMachine.services;

import com.example.vendingMachine.dtos.request.BuyProductRequest;
import com.example.vendingMachine.dtos.request.ProductRequest;
import com.example.vendingMachine.dtos.response.BuyProductResponse;
import com.example.vendingMachine.dtos.response.ProductResponse;
import com.example.vendingMachine.exceptions.InsufficientDeposit;
import com.example.vendingMachine.exceptions.OutOfStock;
import com.example.vendingMachine.models.Product;
import com.example.vendingMachine.models.User;
import com.example.vendingMachine.repositories.ProductRepository;
import com.example.vendingMachine.repositories.UserRepository;
import com.example.vendingMachine.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public BuyProductResponse buyProduct(BuyProductRequest buyProductRequest, Long authId){
		Product product = productRepository.getById(buyProductRequest.getProductId());
		User user = userRepository.getById(authId);
		Long totalProductsPrice = buyProductRequest.getAmountOfProducts() * product.getCost();

		checkDepositAndAvailability(user, product, buyProductRequest.getAmountOfProducts(), totalProductsPrice);

		List<Long> change = calculateChange(user.getDeposit() - totalProductsPrice);
		user.setDeposit(0L);
		product.setAmountAvailable(product.getAmountAvailable() - buyProductRequest.getAmountOfProducts());

		userRepository.save(user);
		productRepository.save(product);

		return BuyProductResponse.builder()
				.productName(product.getProductName())
				.totalSpent(totalProductsPrice)
				.change(change)
				.build();
	}

	public List<ProductResponse> getProducts(){
		List<ProductResponse> productList = productRepository.findAll()
				.stream()
				.map(Product::fromProductToProductResponse)
				.collect(Collectors.toList());

		log.info("Got list of products: {}", productList);
		return productList;
	}

	public ProductResponse createProduct(ProductRequest productRequest, Long sellerId){
		Product product = Product.fromProductRequestToProduct(productRequest);
		product.setSellerId(sellerId);

		product = productRepository.save(product);
		log.info("Created new product: {}", product);

		return Product.fromProductToProductResponse(product);
	}

	public ProductResponse updateProduct(ProductRequest productRequest, Long productId, Long authId) {
		Product product = productRepository.getById(productId);

		AuthenticationUtils.verifyAuthUserPermissions(product.getSellerId(), authId);

		product.setProductName(productRequest.getProductName());
		product.setAmountAvailable(productRequest.getAmountAvailable());
		product.setCost(productRequest.getCost());

		productRepository.save(product);
		log.info("Updated product: {}", product);

		return Product.fromProductToProductResponse(product);
	}

	public void deleteProduct(Long productId, Long authId){
		Product product = productRepository.getById(productId);
		AuthenticationUtils.verifyAuthUserPermissions(product.getSellerId(), authId);

		productRepository.deleteById(productId);
		log.info("Product with id:{} deleted", productId);
	}

	private void checkDepositAndAvailability(User user, Product product, Long amountOfProducts, Long totalProductsPrice){
		if(user.getDeposit() < totalProductsPrice){
			throw new InsufficientDeposit("No enough cash!");
		}

		if(product.getAmountAvailable() < amountOfProducts){
			throw new OutOfStock("No stock available!");
		}
	}

	private List<Long> calculateChange(Long userDeposit){
		List<Long> change = new ArrayList<>();
		List<Long> coins = List.of(100L, 50L, 20L, 10L, 5L);
		int i = 0;

		while(userDeposit != 0){
			if(userDeposit >= coins.get(i)){
				change.add(coins.get(i));
				userDeposit = userDeposit - coins.get(i);
			} else {
				i++;
			}
		}

		return change;
	}
}
