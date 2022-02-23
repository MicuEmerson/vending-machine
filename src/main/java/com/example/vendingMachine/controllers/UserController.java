package com.example.vendingMachine.controllers;

import com.example.vendingMachine.dtos.request.UserRequest;
import com.example.vendingMachine.dtos.response.UserResponse;
import com.example.vendingMachine.services.UserService;
import com.example.vendingMachine.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@RequestMapping(method = RequestMethod.GET, value = "/{userId}")
	public ResponseEntity<UserResponse> getUser(@PathVariable("userId") Long userId, Authentication authentication){
		log.info("Get user with id: {}", userId);
		Long authId = AuthenticationUtils.getAuthId(authentication);
		return new ResponseEntity<>(userService.getUser(userId, authId), HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST, value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
		log.info("Create new user: {}", userRequest);
		return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest,
	                                               @PathVariable("userId") Long userId, Authentication authentication){
		log.info("Update user {} with id: {}", userRequest, userId);
		Long authId = AuthenticationUtils.getAuthId(authentication);
		return new ResponseEntity<>(userService.updateUser(userRequest, userId, authId), HttpStatus.NO_CONTENT);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
	public ResponseEntity<UserResponse> deleteUser(@PathVariable("userId") Long userId, Authentication authentication){
		log.info("Delete user with id: {}", userId);
		Long authId = AuthenticationUtils.getAuthId(authentication);
		userService.deleteUser(userId, authId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
