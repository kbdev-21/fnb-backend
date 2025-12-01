package com.example.fnb.order.dto;

import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import com.example.fnb.shared.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Getter
@Setter
public class CreateOrderDto {
    @NotBlank
    private String storeCode;

    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    @Nullable
    private String customerPhoneNum;

    @Email
    @Nullable
    private String customerEmail;

    @Size(max = 50)
    @Nullable
    private String customerName;

    @Nullable
    private String message;

    @NotNull
    private OrderMethod orderMethod;

    @Nullable
    private String destination;

    @Size(max = 50)
    @Nullable
    private String discountCode;

    @Nullable
    private OrderStatus status;

    @Nullable
    private Boolean paid;

    @Nullable
    private PaymentMethod paymentMethod;

    @NotEmpty
    @Valid
    private List<CreateOrderLineDto> lines;
}
