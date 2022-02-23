package com.example.vendingMachine.dtos.request;

import com.example.vendingMachine.models.Role;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest implements Serializable {
	private String username;
	@ToString.Exclude
	private String password;
	@Enumerated(EnumType.STRING)
	private Role role;
}
