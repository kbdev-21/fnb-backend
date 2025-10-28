package com.example.fnb.product.dto;

import com.example.fnb.category.dto.CategoryDto;
import com.example.fnb.image.dto.ImageDto;
import com.example.fnb.product.domain.entity.Option;
import com.example.fnb.product.domain.entity.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private UUID id;
    private String name;
    private String slug;
    private String normalizedName;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal comparePrice;
//    private List<String> imgUrl;
    private List<ImageDto> images;
    private Set<String> unavailableAtStoreCodes;
    private Instant createdAt;
    private UUID categoryId;
    private CategoryDto category;

    private List<Option> options;
    private List<Topping> toppings;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Option {
        private UUID id;
        private String name;
        private List<OptionSelection> selections;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class OptionSelection {
        private UUID id;
        private String value;
        private BigDecimal priceChange;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Topping {
        private UUID id;
        private String name;
        private BigDecimal priceChange;
    }


}
