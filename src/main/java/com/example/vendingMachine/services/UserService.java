package com.example.vendingMachine.services;

import com.example.vendingMachine.dtos.request.DepositCoinRequest;
import com.example.vendingMachine.dtos.request.UserRequest;
import com.example.vendingMachine.dtos.response.UserResponse;
import com.example.vendingMachine.exceptions.BadRequest;
import com.example.vendingMachine.models.User;
import com.example.vendingMachine.repositories.UserRepository;
import com.example.vendingMachine.utils.AuthenticationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@AllArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserResponse getUser(Long userId, Long authId){
		AuthenticationUtils.verifyAuthUserPermissions(userId, authId);

		User user = userRepository.getById(userId);
		return User.fromUserToUserResponse(user);
	}

	public UserResponse createUser(UserRequest userRequest){
		userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		User user = User.fromUserRequestToUser(userRequest);

		try {
			userRepository.save(user);
		} catch (DataAccessException ex) {
			throw new BadRequest("Username already taken");
		}

		return User.fromUserToUserResponse(user);
	}

	public UserResponse updateUser(UserRequest userRequest, Long userId, Long authId){
		AuthenticationUtils.verifyAuthUserPermissions(userId, authId);

		User user = userRepository.getById(userId);

		user.setUsername(userRequest.getUsername());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setRole(user.getRole());

		userRepository.save(user);
		return User.fromUserToUserResponse(user);
	}

	public void deleteUser(Long userId,  Long authId){
		AuthenticationUtils.verifyAuthUserPermissions(userId, authId);

		userRepository.deleteById(userId);
		log.info("User with id:{} deleted", userId);
	}

	public Long depositCoin(DepositCoinRequest depositCoinRequest, Long authId){
		User user = userRepository.getById(authId);
		user.setDeposit(user.getDeposit() + depositCoinRequest.getDeposit().getValue());
		userRepository.save(user);

		log.info("Deposited {} amount, totalAmount: {}", depositCoinRequest.getDeposit(), user.getDeposit());
		return user.getDeposit();
	}

	public void resetDeposit(Long authId){
		User user = userRepository.getById(authId);
		user.setDeposit(0L);
		userRepository.save(user);

		log.info("Successfully reset deposit to 0");
	}
}
