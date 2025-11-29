package com.example.fnb.menu.dto;

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
public class CategoryDto {
    private UUID id;
    private String name;
    private String slug;
    private String normalizedName;
    private String description;
    private String imgUrl;


    private List<CategoryDto> children;
}
