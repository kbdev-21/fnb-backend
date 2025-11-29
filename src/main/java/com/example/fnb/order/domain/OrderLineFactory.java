package com.example.fnb.order.domain;

import com.example.fnb.order.domain.entity.Order;
import com.example.fnb.order.domain.entity.OrderLine;
import com.example.fnb.order.dto.CreateOrderLineDto;
import com.example.fnb.menu.ProductService;
import com.example.fnb.menu.dto.ProductDto;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderLineFactory {
    private final ProductService productService;

    public OrderLineFactory(ProductService productService) {
        this.productService = productService;
    }

    public OrderLine create(CreateOrderLineDto createDto, Order order, String storeCode) {
        var product = productService.getProductById(createDto.getProductId());
        if(product.getUnavailableAtStoreCodes().contains(storeCode)) {
            throw new DomainException(DomainExceptionCode.STORE_NOT_READY);
        };

        var unitPrice = validateAndCalculateLineUnitPrice(createDto, product);
        var lineAmount = unitPrice.multiply(BigDecimal.valueOf(createDto.getQuantity()));

        List<OrderLine.SelectedOption> selectedOptions = createDto.getSelectedOptions().stream().map(o -> {
            var matchedOption = findMatchedOption(product, o.getOptionId());
            var matchedSelection = findMatchedOptionSelection(matchedOption, o.getSelectionId());
            return new OrderLine.SelectedOption(matchedOption.getName(), matchedSelection.getName(), matchedSelection.getPriceChange());
        }).toList();

        List<OrderLine.SelectedTopping> selectedToppings = createDto.getSelectedToppingIds().stream().map(id -> {
            var matchedTopping = findMatchedTopping(product, id);
            return new OrderLine.SelectedTopping(matchedTopping.getName(), matchedTopping.getPriceChange());
        }).toList();

        var newOrderLine = new OrderLine();
        newOrderLine.setId(UUID.randomUUID());
        newOrderLine.setOrder(order);
        newOrderLine.setProductId(product.getId());
        newOrderLine.setProductName(product.getName());
        newOrderLine.setBasePrice(product.getBasePrice());
        newOrderLine.setUnitPrice(unitPrice);
        newOrderLine.setQuantity(createDto.getQuantity());
        newOrderLine.setLineAmount(lineAmount);
        newOrderLine.setSelectedOptions(selectedOptions);
        newOrderLine.setSelectedToppings(selectedToppings);

        return newOrderLine;
    }

    private BigDecimal validateAndCalculateLineUnitPrice(CreateOrderLineDto dto, ProductDto product) {
        var unitPrice = product.getBasePrice();

        for(var selectedOption : dto.getSelectedOptions()) {
            Set<UUID> requiredOptions = product.getOptions().stream()
                .map(ProductDto.Option::getId)
                .collect(Collectors.toSet());
            Set<UUID> dtoOptions = dto.getSelectedOptions().stream()
                .map(CreateOrderLineDto.SelectedOption::getOptionId)
                .collect(Collectors.toSet());
            if(!requiredOptions.equals(dtoOptions)) {
                throw new DomainException(DomainExceptionCode.MISSING_REQUIRED_OPTIONS);
            }

            var matchedOption = findMatchedOption(product, selectedOption.getOptionId());
            var matchedOptionSelection = findMatchedOptionSelection(
                matchedOption,
                selectedOption.getSelectionId()
            );
            unitPrice = unitPrice.add(matchedOptionSelection.getPriceChange());
        }

        for(var selectedToppingId : dto.getSelectedToppingIds()) {
            var matchedTopping = findMatchedTopping(product, selectedToppingId);
            unitPrice = unitPrice.add(matchedTopping.getPriceChange());
        }

        return unitPrice;
    }

    private ProductDto.Option findMatchedOption(
        ProductDto product,
        UUID optionId
    ) {
        var productOptions = product.getOptions();
        return productOptions.stream()
            .filter(o -> o.getId().equals(optionId))
            .findFirst()
            .orElseThrow(
                () -> new DomainException(DomainExceptionCode.INVALID_PRODUCT_INFO)
            );
    }

    private ProductDto.OptionSelection findMatchedOptionSelection(
        ProductDto.Option option,
        UUID selectionId
    ) {
        return option.getSelections().stream()
            .filter(s -> s.getId().equals(selectionId))
            .findFirst()
            .orElseThrow(
                () -> new DomainException(DomainExceptionCode.INVALID_PRODUCT_INFO)
            );
    }

    private ProductDto.Topping findMatchedTopping(
        ProductDto product,
        UUID toppingId
    ) {
        var productToppings = product.getToppings();
        return productToppings.stream()
            .filter(t -> t.getId().equals(toppingId))
            .findFirst()
            .orElseThrow(
                () -> new DomainException(DomainExceptionCode.INVALID_PRODUCT_INFO)
            );
    }
}
