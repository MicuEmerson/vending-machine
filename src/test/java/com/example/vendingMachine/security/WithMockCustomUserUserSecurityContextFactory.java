package com.example.vendingMachine.security;

import com.example.vendingMachine.models.Role;
import com.example.vendingMachine.models.User;
import com.example.vendingMachine.models.security.CustomSecurityUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserUserSecurityContextFactory
		implements WithSecurityContextFactory<WithMockCustomUser> {
	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();

		User principal = User.builder()
				.id(1L)
				.username("user1")
				.password("pass123")
				.role(Role.SELLER)
				.build();

		CustomSecurityUser securityUser = new CustomSecurityUser(principal);
		Authentication auth = new UsernamePasswordAuthenticationToken(
				securityUser, "pass123", securityUser.getAuthorities() );
		context.setAuthentication(auth);
		return context;
	}
}