package com.example.fnb.menu.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ProductCreateDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal basePrice;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal comparePrice;

    @NotEmpty
    private List<@NotBlank String> imgUrls;

    @NotNull
    private UUID categoryId;

    @Valid
    @NotNull
    private List<@Valid OptionCreateDto> options;

    @Valid
    @NotNull
    private List<@Valid ToppingCreateDto> toppings;
}
