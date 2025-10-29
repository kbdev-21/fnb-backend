package com.example.fnb.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private List<UUID> childrenIds;

    private UUID parentId;

}
