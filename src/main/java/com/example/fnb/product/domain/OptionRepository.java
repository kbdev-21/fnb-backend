package com.example.fnb.product.domain;

import com.example.fnb.product.domain.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OptionRepository extends JpaRepository<Option, UUID> {
}
