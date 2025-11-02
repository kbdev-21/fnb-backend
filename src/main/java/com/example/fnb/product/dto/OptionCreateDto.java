package com.example.fnb.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OptionCreateDto {

    @NotBlank
    @Size(max = 255)
    private String name;

    @Valid
    @NotEmpty
    private List<@Valid OptionCreateDtoSelection> selections;
}
