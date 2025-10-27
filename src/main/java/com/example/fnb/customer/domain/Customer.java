package com.example.fnb.customer.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String phoneNum;

    @Column(unique = true)
    @Nullable
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String normalizedName;

    @Nullable
    private UUID userId;

    @Column(nullable = false)
    private BigDecimal moneySpent;

    @Column(nullable = false)
    private int loyaltyPoints;

    @Column(nullable = false)
    private Instant createdAt;
}
