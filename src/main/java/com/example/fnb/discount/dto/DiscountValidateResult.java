package com.example.fnb.discount.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class DiscountValidateResult {
    @Nullable
    private String applicableDiscountCode;
    private BigDecimal discountAmount;
}
