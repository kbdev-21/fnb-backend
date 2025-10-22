package com.example.fnb.order.domain.entity;

import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import com.example.fnb.shared.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String customerPhoneNum;

    @Nullable
    private String customerEmail;

    @Column(nullable = false)
    private String customerFirstName;

    @Column(nullable = false)
    private String customerLastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderMethod orderMethod;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Nullable
    private String discountCode;

    @Column(nullable = false)
    private BigDecimal subtotalAmount;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private BigDecimal deliveryFee;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private boolean paid;

    @Enumerated(EnumType.STRING)
    @Nullable
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private Instant createdAt;

    @Nullable
    private Instant completedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<OrderLine> lines;
}
