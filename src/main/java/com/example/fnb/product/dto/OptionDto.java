package com.example.fnb.product.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OptionDto {
    private UUID id;
    private String name;
    private UUID productId;
    private List<OptionDto.Selection> selections;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Selection {
        private UUID id;
        private String name;
        private BigDecimal priceChange;
    }
}
