package com.example.fnb.order.dto;

import com.example.fnb.shared.enums.OrderMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderPreviewDto {
    private String storeCode;
    private OrderMethod orderMethod;
    @Nullable
    private String discountCode;
    private BigDecimal subtotalAmount;
    private BigDecimal discountAmount;
    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private List<CartDtoLine> lines;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CartDtoLine {
        private UUID productId;
        private String productName;
        private String productImgUrl;
        private List<CartDtoLineSelectedOption> selectedOptions;
        private List<CartDtoLineSelectedTopping> selectedToppings;
        private BigDecimal basePrice;
        private BigDecimal unitPrice;
        private int quantity;
        private BigDecimal lineAmount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CartDtoLineSelectedOption {
        private String name;
        private String selection;
        private BigDecimal priceChange;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CartDtoLineSelectedTopping {
        private String name;
        private BigDecimal priceChange;
    }
}
