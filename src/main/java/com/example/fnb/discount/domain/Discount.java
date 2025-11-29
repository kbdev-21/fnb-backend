package com.example.fnb.discount.domain;

import com.example.fnb.shared.enums.DiscountType;
import jakarta.persistence.*;
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
@Table(name = "discounts")
@Getter
@Setter
public class Discount {
    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Nullable
    private BigDecimal maxFixedAmount;

    @Nullable
    private BigDecimal minApplicablePrice;

    @Column(nullable = false)
    private boolean used;

    @Nullable
    private String usedByPhoneNum;

    @Column(nullable = false)
    private Instant createdAt;

    @Nullable
    private Instant expiredAt;
}
