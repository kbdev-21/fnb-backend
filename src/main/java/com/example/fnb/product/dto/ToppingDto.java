package com.example.fnb.product.dto;

import com.example.fnb.product.domain.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.util.UUID;

public class ToppingDto {
    private UUID id;
    private String name;
    private BigDecimal priceChange;
    private UUID productId;
}
