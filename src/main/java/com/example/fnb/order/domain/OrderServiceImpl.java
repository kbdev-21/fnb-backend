package com.example.fnb.order.domain;

import com.example.fnb.discount.DiscountService;
import com.example.fnb.order.OrderCreatedEvent;
import com.example.fnb.order.OrderService;
import com.example.fnb.order.domain.entity.Order;
import com.example.fnb.order.domain.entity.OrderLine;
import com.example.fnb.order.domain.repository.OrderLineRepository;
import com.example.fnb.order.domain.repository.OrderRepository;
import com.example.fnb.order.dto.OrderPreviewDto;
import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import com.example.fnb.product.ProductService;
import com.example.fnb.product.dto.ProductDto;
import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import com.example.fnb.store.StoreService;
import com.example.fnb.store.dto.StoreDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final ProductService productService;
    private final StoreService storeService;
    private final DiscountService discountService;

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(ProductService productService, StoreService storeService, DiscountService discountService, OrderRepository orderRepository, OrderLineRepository orderLineRepository, ApplicationEventPublisher eventPublisher, ModelMapper modelMapper) {
        this.productService = productService;
        this.storeService = storeService;
        this.discountService = discountService;
        this.orderRepository = orderRepository;
        this.orderLineRepository = orderLineRepository;
        this.eventPublisher = eventPublisher;
        this.modelMapper = modelMapper;
    }

    @Override
    public OrderPreviewDto previewOrder(CreateOrderDto dto) {
        var store = storeService.getStoreByCode(dto.getStoreCode());
        if(!store.isOpen()) {
            throw new DomainException(DomainExceptionCode.STORE_NOT_READY);
        }

        var newOrder = new Order();

        var lines = dto.getLines().stream().map(lineDto ->
            createOrderLineFromDto(newOrder, store.getCode(), lineDto)
        ).toList();

        var subtotalAmount = BigDecimal.ZERO;
        for(var line : lines) {
            subtotalAmount = subtotalAmount.add(line.getLineAmount());
        }

        String discountCode = null;
        var discountAmount = BigDecimal.ZERO;
        if(dto.getDiscountCode() != null) {
            discountAmount = discountService.validateAndCalculateDiscountAmount(
                dto.getDiscountCode(),
                subtotalAmount,
                dto.getCustomerPhoneNum()
            );
            discountCode = dto.getDiscountCode();
        }

        var deliveryFee = calculateDeliveryFee(dto.getOrderMethod());

        var totalAmount = subtotalAmount.add(deliveryFee).subtract(discountAmount);

        newOrder.setLines(lines);
        newOrder.setStoreCode(store.getCode());
        newOrder.setCustomerPhoneNum(dto.getCustomerPhoneNum());
        newOrder.setCustomerFirstName(dto.getCustomerFirstName());
        newOrder.setCustomerLastName(dto.getCustomerLastName());
        newOrder.setOrderMethod(dto.getOrderMethod());
        newOrder.setDestination(getDestination(dto, store));
        newOrder.setDiscountCode(discountCode);
        newOrder.setSubtotalAmount(subtotalAmount);
        newOrder.setDiscountAmount(discountAmount);
        newOrder.setDeliveryFee(deliveryFee);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setCreatedAt(Instant.now());

        return modelMapper.map(newOrder, OrderPreviewDto.class);
    }

    @Override
    public OrderDto createOrder(@Valid CreateOrderDto dto) {
        var store = storeService.getStoreByCode(dto.getStoreCode());
        if(!store.isOpen()) {
            throw new DomainException(DomainExceptionCode.STORE_NOT_READY);
        }

        var newOrder = new Order();

        var lines = dto.getLines().stream().map(lineDto ->
            createOrderLineFromDto(newOrder, store.getCode(), lineDto)
        ).toList();

        var subtotalAmount = BigDecimal.ZERO;
        for(var line : lines) {
            subtotalAmount = subtotalAmount.add(line.getLineAmount());
        }

        String discountCode = null;
        var discountAmount = BigDecimal.ZERO;
        if(dto.getDiscountCode() != null) {
            discountAmount = discountService.validateAndCalculateDiscountAmount(
                dto.getDiscountCode(),
                subtotalAmount,
                dto.getCustomerPhoneNum()
            );
            discountCode = dto.getDiscountCode();
        }

        var deliveryFee = calculateDeliveryFee(dto.getOrderMethod());

        var totalAmount = subtotalAmount.add(deliveryFee).subtract(discountAmount);

        newOrder.setId(UUID.randomUUID());
        newOrder.setLines(lines);
        newOrder.setStoreCode(store.getCode());
        newOrder.setCustomerPhoneNum(dto.getCustomerPhoneNum());
        newOrder.setCustomerFirstName(dto.getCustomerFirstName());
        newOrder.setCustomerLastName(dto.getCustomerLastName());
        newOrder.setOrderMethod(dto.getOrderMethod());
        newOrder.setDestination(getDestination(dto, store));
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setDiscountCode(discountCode);
        newOrder.setSubtotalAmount(subtotalAmount);
        newOrder.setDiscountAmount(discountAmount);
        newOrder.setDeliveryFee(deliveryFee);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setPaid(false);
        newOrder.setCreatedAt(Instant.now());

        var savedOrder = orderRepository.save(newOrder);
        var mappedOrderDto = modelMapper.map(savedOrder, OrderDto.class);

        eventPublisher.publishEvent(new OrderCreatedEvent(this, mappedOrderDto));
        return mappedOrderDto;
    }

    @Override
    public List<OrderDto> getOrders() {
        var orders = orderRepository.findAll();
        return orders.stream()
            .map(o -> modelMapper.map(o, OrderDto.class))
            .toList();
    }

    @Override
    public OrderDto updateOrderStatus(UUID orderId, OrderStatus status) {
        return null;
    }

    /* TODO: temporary just for testing */
    private BigDecimal calculateDeliveryFee(OrderMethod method) {
        return method == OrderMethod.DELIVERY ? new BigDecimal("10000") : BigDecimal.ZERO;
    }

    private String getDestination(CreateOrderDto dto, StoreDto store) {
        if(dto.getOrderMethod() == OrderMethod.DELIVERY) {
            if(dto.getDestination() == null) throw new DomainException(DomainExceptionCode.ADDRESS_IS_INVALID);
            return dto.getDestination();
        }
        if(dto.getOrderMethod() == OrderMethod.TAKE_AWAY) {
            return store.getCode();
        }
        var matchedTable = store.getTables().stream()
            .filter(t -> t.getCode().equals(dto.getDestination()))
            .findFirst()
            .orElseThrow(() -> new DomainException(DomainExceptionCode.ADDRESS_IS_INVALID));
        return store.getCode() + "-" + matchedTable.getCode();
    }

    private OrderLine createOrderLineFromDto(Order order, String storeCode, CreateOrderDto.Line dto) {
        var product = productService.getProductById(dto.getProductId());
        if(product.getUnavailableAtStoreCodes().contains(storeCode)) {
            throw new DomainException(DomainExceptionCode.STORE_NOT_READY);
        };

        var unitPrice = calculateLineUnitPrice(dto, product);
        var lineAmount = unitPrice.multiply(BigDecimal.valueOf(dto.getQuantity()));

        List<OrderLine.SelectedOption> selectedOptions = dto.getSelectedOptions().stream().map( o -> {
            var matchedOption = findMatchedOption(product, o.getOptionId());
            var matchedSelection = findMatchedOptionSelection(matchedOption, o.getSelectionId());
            return new OrderLine.SelectedOption(matchedOption.getName(), matchedSelection.getValue(), matchedSelection.getPriceChange());
        }).toList();

        List<OrderLine.SelectedTopping> selectedToppings = dto.getSelectedToppingIds().stream().map(id -> {
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
        newOrderLine.setQuantity(dto.getQuantity());
        newOrderLine.setLineAmount(lineAmount);
        newOrderLine.setSelectedOptions(selectedOptions);
        newOrderLine.setSelectedToppings(selectedToppings);

        return newOrderLine;
    }

    private BigDecimal calculateLineUnitPrice(CreateOrderDto.Line dto, ProductDto product) {
        var unitPrice = product.getBasePrice();

        for(var selectedOption : dto.getSelectedOptions()) {
            Set<UUID> requiredOptions = product.getOptions().stream()
                .map(ProductDto.ProductDtoOption::getId)
                .collect(Collectors.toSet());
            Set<UUID> dtoOptions = dto.getSelectedOptions().stream()
                .map(CreateOrderDto.LineSelectedOption::getOptionId)
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

    private ProductDto.ProductDtoOption findMatchedOption(
        ProductDto product,
        UUID optionId
    ) {
        var productOptions = product.getOptions();
        return productOptions.stream()
            .filter(o -> o.getId().equals(optionId))
            .findFirst()
            .orElseThrow(
                () -> new DomainException(DomainExceptionCode.PRODUCT_INFO_IS_INVALID)
            );
    }

    private ProductDto.ProductDtoOptionSelection findMatchedOptionSelection(
        ProductDto.ProductDtoOption option,
        UUID selectionId
    ) {
        return option.getSelections().stream()
            .filter(s -> s.getId().equals(selectionId))
            .findFirst()
            .orElseThrow(
                () -> new DomainException(DomainExceptionCode.PRODUCT_INFO_IS_INVALID)
            );
    }

    private ProductDto.ProductDtoTopping findMatchedTopping(
        ProductDto product,
        UUID toppingId
    ) {
        var productToppings = product.getToppings();
        return productToppings.stream()
            .filter(t -> t.getId().equals(toppingId))
            .findFirst()
            .orElseThrow(
                () -> new DomainException(DomainExceptionCode.PRODUCT_INFO_IS_INVALID)
            );
    }
}
