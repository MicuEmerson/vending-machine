package com.example.vendingMachine.repositories;

import com.example.vendingMachine.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByUsername(String username);
}
