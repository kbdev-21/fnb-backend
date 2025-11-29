package com.example.fnb.menu.domain.repository;

import com.example.fnb.menu.domain.entity.Topping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ToppingRepository extends JpaRepository<Topping, UUID> {
}
