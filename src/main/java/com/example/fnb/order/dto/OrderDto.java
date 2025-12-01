package com.example.fnb.order.dto;

import com.example.fnb.order.domain.entity.Order;
import com.example.fnb.order.domain.entity.OrderLine;
import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import com.example.fnb.shared.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {
    private UUID id;
    private String storeCode;
    @Nullable
    private String customerPhoneNum;
    @Nullable
    private String customerEmail;
    private String customerName;
    @Nullable
    private String message;
    private OrderMethod orderMethod;
    private String destination;
    private OrderStatus status;
    @Nullable
    private String discountCode;
    private BigDecimal subtotalAmount;
    private BigDecimal discountAmount;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;
    private boolean paid;
    @Nullable
    private PaymentMethod paymentMethod;
    private Instant createdAt;
    @Nullable
    private Instant completedAt;
    private List<OrderDtoLine> lines;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrderDtoLine {
        private UUID id;
        private UUID productId;
        private String productName;
        private String productImgUrl;
        private List<OrderDtoLineSelectedOption> selectedOptions;
        private List<OrderDtoLineSelectedTopping> selectedToppings;
        private BigDecimal basePrice;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal lineAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrderDtoLineSelectedOption {
        private String name;
        private String selection;
        private BigDecimal priceChange;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OrderDtoLineSelectedTopping {
        private String name;
        private BigDecimal priceChange;
    }
}
