package com.example.fnb.collection.dto;

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
public class CollectionDto {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private String imgUrl;

    private List<UUID> productIds;
}
