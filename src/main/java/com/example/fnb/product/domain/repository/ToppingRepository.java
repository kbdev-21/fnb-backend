package com.example.fnb.product.domain.repository;

import com.example.fnb.product.domain.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ToppingRepository extends JpaRepository<Topping, UUID> {
}
