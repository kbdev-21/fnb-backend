package com.example.fnb.order.domain.repository;

import com.example.fnb.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findAllByStoreCode(String storeCode);
}
