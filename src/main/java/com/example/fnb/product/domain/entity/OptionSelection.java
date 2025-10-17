package com.example.fnb.product.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "option_selection")
@Getter
@Setter
public class OptionSelection {

    @Id
    private UUID id = UUID.randomUUID();

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private BigDecimal priceChange;

    @ManyToOne
    @JoinColumn(nullable = false, name = "option_id")
    private Option option;
}
