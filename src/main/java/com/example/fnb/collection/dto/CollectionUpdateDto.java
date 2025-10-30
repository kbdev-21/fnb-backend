package com.example.fnb.collection.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionUpdateDto {

    @Nullable
    @Size(max = 255)
    private String name;

    @Nullable
    @Size(max = 2000)
    private String description;

    @Nullable
    private String imgUrl;

}
