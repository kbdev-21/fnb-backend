package com.example.fnb.order.dto;

import com.example.fnb.shared.enums.OrderMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderDto {
    @NotBlank
    private String storeCode;

    @Pattern(regexp = "^\\+?[0-9]{1,3}?[- ]?[0-9]{3,4}[- ]?[0-9]{3,4}$")
    private String customerPhoneNum;

    @Size(max = 50)
    private String customerFirstName;

    @Size(max = 50)
    private String customerLastName;

    @NotNull
    private OrderMethod orderMethod;

    @Size(max = 255)
    @Nullable
    private String destination;

    @Size(max = 50)
    @Nullable
    private String discountCode;

    @NotEmpty
    @Valid
    private List<Line> lines;

    @Getter
    @Setter
    public static class Line {
        @NotNull
        private UUID productId;

        @Valid
        private List<LineSelectedOption> selectedOptions;

        private List<UUID> selectedToppingIds;

        @Min(1)
        private int quantity;
    }

    @Getter
    @Setter
    public static class LineSelectedOption {
        @NotNull
        private UUID optionId;

        @NotNull
        private UUID selectionId;
    }
}
