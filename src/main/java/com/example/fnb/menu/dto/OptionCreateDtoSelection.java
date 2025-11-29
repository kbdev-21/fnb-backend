package com.example.fnb.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OptionCreateDtoSelection {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private BigDecimal priceChange;
}
