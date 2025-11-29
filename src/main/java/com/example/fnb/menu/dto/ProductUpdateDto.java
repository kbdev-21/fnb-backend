package com.example.fnb.menu.dto;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductUpdateDto {
    @Nullable
    @Size(max = 255)
    private String name;

    @Nullable
    @Size(max = 2000)
    private String description;

    @Nullable
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal basePrice;

    @Nullable
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal comparePrice;

    @Nullable
    private List<String> imgUrls;

    @Nullable
    private UUID categoryId;

    @Nullable
    private List<OptionCreateDto> options;

    @Nullable
    private List<ToppingCreateDto> toppings;
}
