package com.example.fnb.collection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CollectionCreateDto {

    @NotBlank
    @Size(max = 255)
    private String name;
    private String slug;

    @Size(max = 2000)
    private String description;

    @NotEmpty
    private String imgUrl;

    private List<UUID> productIds;
}
