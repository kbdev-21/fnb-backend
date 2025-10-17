package com.example.fnb.product.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductCreateDtoTopping {

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private BigDecimal priceChange;
}
