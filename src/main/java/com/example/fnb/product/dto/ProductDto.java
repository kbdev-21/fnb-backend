package com.example.fnb.product.dto;

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

    private List<ProductDtoOption> options;
    private List<ProductDtoTopping> toppings;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductDtoOption {
        private UUID id;
        private String name;
        private List<ProductDtoOptionSelection> selections;
    }


    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductDtoOptionSelection {
        private UUID id;
        private String value;
        private BigDecimal priceChange;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductDtoTopping {
        private UUID id;
        private String name;
        private BigDecimal priceChange;
    }


}
