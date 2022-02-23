package com.example.vendingMachine.services.security;

import com.example.vendingMachine.models.User;
import com.example.vendingMachine.models.security.CustomSecurityUser;
import com.example.vendingMachine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public CustomSecurityUser loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findOneByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("Problem during authentication"));

		return new CustomSecurityUser(user);
	}
}
