package com.example.fnb.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CreateOrderLineDto {
    @NotNull
    private UUID productId;

    @Valid
    private List<SelectedOption> selectedOptions;

    private List<UUID> selectedToppingIds;

    @Min(1)
    private int quantity;

    @Getter
    @Setter
    public static class SelectedOption {
        @NotNull
        private UUID optionId;

        @NotNull
        private UUID selectionId;
    }
}
