package com.example.fnb.store.domain.repository;

import com.example.fnb.store.domain.entity.StoreTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StoreTableRepository extends JpaRepository<StoreTable, UUID> {
}
