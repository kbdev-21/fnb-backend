package com.example.fnb.product.domain;

import com.example.fnb.product.domain.entity.Product;
import com.example.fnb.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findBySlug(String slug);

}
