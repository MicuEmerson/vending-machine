package com.example.vendingMachine.services;

import com.example.vendingMachine.dtos.request.BuyProductRequest;
import com.example.vendingMachine.dtos.response.BuyProductResponse;
import com.example.vendingMachine.exceptions.InsufficientDeposit;
import com.example.vendingMachine.exceptions.OutOfStock;
import com.example.vendingMachine.models.Product;
import com.example.vendingMachine.models.User;
import com.example.vendingMachine.repositories.ProductRepository;
import com.example.vendingMachine.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ProductService.class})
public class ProductServiceTest {
	@MockBean
	private ProductRepository productRepository;
	@MockBean
	private UserRepository userRepository;
	@Autowired
	private ProductService productService;

	private static Long AUTH_ID = 1L;
	private static Long PRODUCT_ID = 1L;
	private static String PRODUCT_NAME = "Chocolate";

	@Test
	public void testBuy() {
		// given
		BuyProductRequest request = createBuyProductRequest(PRODUCT_ID, 5L);
		User user = createUser(100L);
		Product product = createProduct(5L, 15L, PRODUCT_NAME);

		BuyProductResponse expectedResponse = BuyProductResponse.builder()
				.change(List.of(50L, 20L, 5L))
				.totalSpent(25L)
				.productName(PRODUCT_NAME)
				.build();

		// when
		Mockito.when(userRepository.getById(AUTH_ID)).thenReturn(user);
		Mockito.when(productRepository.getById(PRODUCT_ID)).thenReturn(product);
		BuyProductResponse actualResponse = productService.buyProduct(request, AUTH_ID);

		// then
		Assert.assertEquals(actualResponse, expectedResponse);
	}

	@Test(expected = OutOfStock.class)
	public void testBuyOutOfStockException() {
		// given
		BuyProductRequest request = createBuyProductRequest(PRODUCT_ID, 10L);
		User user = createUser(100L);
		Product product = createProduct(5L, 5L, PRODUCT_NAME);

		// when
		Mockito.when(userRepository.getById(AUTH_ID)).thenReturn(user);
		Mockito.when(productRepository.getById(PRODUCT_ID)).thenReturn(product);
		BuyProductResponse actualResponse = productService.buyProduct(request, AUTH_ID);
	}

	@Test(expected = InsufficientDeposit.class)
	public void testBuyInsufficientDepositException() {
		// givenOf
		BuyProductRequest request = createBuyProductRequest(PRODUCT_ID, 10L);
		User user = createUser(5L);
		Product product = createProduct(5L, 5L, PRODUCT_NAME);

		// when
		Mockito.when(userRepository.getById(AUTH_ID)).thenReturn(user);
		Mockito.when(productRepository.getById(PRODUCT_ID)).thenReturn(product);
		BuyProductResponse actualResponse = productService.buyProduct(request, AUTH_ID);
	}


	private BuyProductRequest createBuyProductRequest(Long productId, Long amount){
		return BuyProductRequest.builder()
				.productId(productId)
				.amountOfProducts(amount)
				.build();
	}

	private Product createProduct(Long cost, Long amount, String name){
		return Product.builder()
				.cost(cost)
				.id(PRODUCT_ID)
				.amountAvailable(amount)
				.productName(name)
				.build();
	}

	private User createUser(Long deposit){
		return User.builder()
				.deposit(deposit)
				.build();
	}
}
