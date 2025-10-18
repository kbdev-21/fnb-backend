package com.example.fnb.store.domain.repository;

import com.example.fnb.store.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepository extends JpaRepository<Store, UUID> {
    Optional<Store> findByCode(String code);
}
