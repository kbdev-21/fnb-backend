package com.example.fnb.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "toppings")
@Getter
@Setter
public class Topping {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private BigDecimal priceChange;

    @ManyToOne
    @JoinColumn(nullable = false, name = "product_id")
    private Product product;
}
