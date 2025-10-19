package com.example.fnb.discount.dto;

import com.example.fnb.shared.enums.DiscountType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class CreateDiscountDto {
    @NotBlank
    @Size(max = 50)
    private String code;

    @NotNull
    private DiscountType discountType;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal discountValue;

    @Nullable
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal maxFixedAmount;

    @Nullable
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal minApplicablePrice;

    @Nullable
    @Min(1)
    private Integer globalUsageLimit;

    @NotNull
    private boolean useOncePerCustomer;

    @NotNull
    private boolean active;

    @Future
    @Nullable
    private Instant expiredAt;
}
