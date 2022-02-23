package com.example.vendingMachine.controllers;

import com.example.vendingMachine.dtos.request.BuyProductRequest;
import com.example.vendingMachine.dtos.request.DepositCoinRequest;
import com.example.vendingMachine.dtos.request.ProductRequest;
import com.example.vendingMachine.dtos.response.BuyProductResponse;
import com.example.vendingMachine.dtos.response.ProductResponse;
import com.example.vendingMachine.services.ProductService;
import com.example.vendingMachine.services.UserService;
import com.example.vendingMachine.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;
	private final UserService userService;

	@RequestMapping(method = RequestMethod.POST, value = "/buy", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BuyProductResponse> buyProduct(@RequestBody BuyProductRequest buyProductRequest, Authentication authentication){
		log.info("Buy product {}", buyProductRequest);
		Long authId = AuthenticationUtils.getAuthId(authentication);
		return new ResponseEntity<>(productService.buyProduct(buyProductRequest, authId), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> depositCoin(@RequestBody DepositCoinRequest depositCoinRequest, Authentication authentication){
		Long authId = AuthenticationUtils.getAuthId(authentication);
		log.info("Deposit coin of value {} for userId: {}", depositCoinRequest, authId);
		userService.depositCoin(depositCoinRequest, authId);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/reset")
	public ResponseEntity<Void> resetDeposit(Authentication authentication){
		Long authId = AuthenticationUtils.getAuthId(authentication);
		log.info("Reset deposit for user with id {}", authId);
		userService.resetDeposit(authId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/products")
	public ResponseEntity<List<ProductResponse>> getProducts(){
		log.info("Get products");
		return new ResponseEntity<>(productService.getProducts(), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/products", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest, Authentication authentication){
		Long authId = AuthenticationUtils.getAuthId(authentication);
		log.info("Add product:{} for seller with id: {}", productRequest, authId);
		ProductResponse productResponse = productService.createProduct(productRequest, authId);
		return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/products/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ProductResponse> updateProduct(@PathVariable("productId") Long productId,
	                                                     @RequestBody ProductRequest productRequest,
	                                                     Authentication authentication){
		log.info("Update product: {} with id: {}", productRequest, productId);
		Long authId = AuthenticationUtils.getAuthId(authentication);
		ProductResponse productResponse = productService.updateProduct(productRequest, productId, authId);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/products/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable("productId") Long productId, Authentication authentication){
		log.info("Delete product with id: {}", productId);
		Long authId = AuthenticationUtils.getAuthId(authentication);
		productService.deleteProduct(productId, authId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
