package com.example.fnb.product.dto;

import com.example.fnb.product.domain.entity.Option;
import com.example.fnb.product.domain.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
        private String value;
        private BigDecimal priceChange;
    }
}
