package com.example.fnb.product.domain.repository;

import com.example.fnb.product.domain.entity.OptionSelection;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OptionSelectionRepository extends JpaRepository<OptionSelection, UUID> {
}
