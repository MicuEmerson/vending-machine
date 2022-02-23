package com.example.vendingMachine.models;

import com.example.vendingMachine.dtos.request.UserRequest;
import com.example.vendingMachine.dtos.response.UserResponse;
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
public class User {
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@Column(unique=true)
	private String username;

	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	private Long deposit;

	public static User fromUserRequestToUser(UserRequest userRequest){
		return User.builder()
				.deposit(0L)
				.username(userRequest.getUsername())
				.password(userRequest.getPassword()) // TODO Encrypt
				.role(userRequest.getRole())
				.build();
	}

	public static UserResponse fromUserToUserResponse(User user){
		return UserResponse.builder()
				.id(user.getId())
				.username(user.getUsername())
				.role(user.getRole())
				.build();
	}
}
