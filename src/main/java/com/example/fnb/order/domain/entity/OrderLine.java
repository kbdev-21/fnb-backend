package com.example.fnb.order.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
@Getter
@Setter
public class OrderLine {
    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "order_id")
    private Order order;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private String productName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<SelectedOption> selectedOptions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private List<SelectedTopping> selectedToppings;

    @Column(nullable = false)
    private BigDecimal basePrice;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal lineAmount;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectedOption {
        private String name;
        private String selection;
        private BigDecimal priceChange;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectedTopping {
        private String name;
        private BigDecimal priceChange;
    }
}
