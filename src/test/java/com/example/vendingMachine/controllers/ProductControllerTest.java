package com.example.vendingMachine.controllers;

import com.example.vendingMachine.dtos.request.ProductRequest;
import com.example.vendingMachine.dtos.response.ProductResponse;
import com.example.vendingMachine.repositories.UserRepository;
import com.example.vendingMachine.security.WithMockCustomUser;
import com.example.vendingMachine.services.ProductService;
import com.example.vendingMachine.services.UserService;
import com.example.vendingMachine.services.security.JpaUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {
	@MockBean
	ProductService productService;

	@MockBean
	JpaUserDetailsService jpaUserDetailsService;

	@MockBean
	UserRepository userRepository;

	@MockBean
	UserService userService;

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper mapper;

	@Test
	public void testGetProducts() throws Exception {
		// given
		List<ProductResponse> productResponses = List.of(ProductResponse.builder()
				.id(1L)
				.cost(5L)
				.amountAvailable(3L)
				.productName("product1")
				.build());

		// when
		Mockito.when(productService.getProducts()).thenReturn(productResponses);

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.get("/products")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	@WithMockCustomUser
	public void testCreateProduct() throws Exception {
		// given
		ProductRequest productRequest = ProductRequest.builder()
				.productName("product1")
				.amountAvailable(2L)
				.cost(5L)
				.build();

		ProductResponse productResponse = ProductResponse.builder()
				.productName("product1")
				.id(1L)
				.amountAvailable(2L)
				.cost(5L)
				.build();

		// when
		Mockito.when(productService.createProduct(productRequest, 1L)).thenReturn(productResponse);

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/products")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(productRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.productName", is(productResponse.getProductName())));
	}

	@Test
	@WithAnonymousUser
	public void testCreateProductsNotAuthorize() throws Exception {
		// given
		ProductRequest productRequest = ProductRequest.builder()
				.productName("product1")
				.amountAvailable(2L)
				.cost(5L)
				.build();

		ProductResponse productResponse = ProductResponse.builder()
				.productName("product1")
				.id(1L)
				.amountAvailable(2L)
				.cost(5L)
				.build();

		// when
		Mockito.when(productService.createProduct(productRequest, 1L)).thenReturn(productResponse);

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/products")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(productRequest)))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockCustomUser
	public void testUpdateProduct() throws Exception {
		// given
		ProductRequest productRequest = ProductRequest.builder()
				.productName("product1")
				.amountAvailable(3L)
				.cost(5L)
				.build();

		ProductResponse productResponse = ProductResponse.builder()
				.productName("product1")
				.id(1L)
				.amountAvailable(3L)
				.cost(5L)
				.build();

		// when
		Mockito.when(productService.updateProduct(productRequest, 1L, 1L)).thenReturn(productResponse);

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.put("/products/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(productRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.amountAvailable", is(productResponse.getAmountAvailable())));
	}

	@Test
	@WithMockCustomUser
	public void testUpdateProductForbidden() throws Exception {
		// given
		ProductRequest productRequest = ProductRequest.builder()
				.productName("product1")
				.amountAvailable(3L)
				.cost(5L)
				.build();

		ProductResponse productResponse = ProductResponse.builder()
				.productName("product1")
				.id(1L)
				.amountAvailable(3L)
				.cost(5L)
				.build();

		// when
		Mockito.when(productService.updateProduct(productRequest, 1L, 1L)).thenReturn(productResponse);

		// then
		mockMvc.perform(MockMvcRequestBuilders
				.put("/products/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(productRequest)))
				.andExpect(status().isOk());
	}
}
