package com.example.fnb.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

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
    private List<@NotBlank String> imgUrl;

    @Valid
    @NotNull
    private List<@Valid ProductCreateDtoOption> options;

    @Valid
    @NotNull
    private List<@Valid ProductCreateDtoTopping> toppings;
}
