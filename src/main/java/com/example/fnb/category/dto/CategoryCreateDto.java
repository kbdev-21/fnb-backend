package com.example.fnb.category.dto;

import com.example.fnb.category.domain.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateDto {

    @NotBlank
    @Size(max = 255)
    private String name;
    private String slug;
    private String normalizedName;

    @Size(max = 2000)
    private String description;

    @NotEmpty
    private String imgUrl;

    private List<UUID> childrenIds;

}
