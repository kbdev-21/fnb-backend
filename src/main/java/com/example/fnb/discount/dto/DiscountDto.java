package com.example.fnb.discount.dto;

import com.example.fnb.shared.enums.DiscountType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class DiscountDto {
    private UUID id;
    private String code;
    private DiscountType discountType;
    private BigDecimal discountValue;
    @Nullable
    private BigDecimal maxFixedAmount;
    @Nullable
    private BigDecimal minApplicablePrice;
    private boolean used;
    @Nullable
    private String usedByPhoneNum;
    private Instant createdAt;
    @Nullable
    private Instant expiredAt;
}
