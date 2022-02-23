package com.example.vendingMachine.repositories;

import com.example.vendingMachine.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
