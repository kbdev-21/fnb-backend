package com.example.fnb.menu.domain.repository;

import com.example.fnb.menu.domain.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID> {
}
