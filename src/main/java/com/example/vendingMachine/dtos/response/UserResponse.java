package com.example.vendingMachine.dtos.response;

import com.example.vendingMachine.models.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
	private Long id;
	private String username;
	@Enumerated(EnumType.STRING)
	private Role role;
}
