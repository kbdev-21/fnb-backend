package com.example.fnb.order.domain;

import com.example.fnb.discount.DiscountService;
import com.example.fnb.order.event.OrderCreatedEvent;
import com.example.fnb.order.OrderService;
import com.example.fnb.order.domain.entity.Order;
import com.example.fnb.order.domain.repository.OrderRepository;
import com.example.fnb.order.dto.OrderPreviewDto;
import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import com.example.fnb.shared.enums.PaymentMethod;
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
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private final StoreService storeService;
    private final DiscountService discountService;

    private final OrderLineFactory orderLineFactory;
    private final OrderRepository orderRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final ModelMapper modelMapper;

    public OrderServiceImpl(StoreService storeService, DiscountService discountService, OrderLineFactory orderLineFactory, OrderRepository orderRepository, ApplicationEventPublisher eventPublisher, ModelMapper modelMapper) {
        this.storeService = storeService;
        this.discountService = discountService;
        this.orderLineFactory = orderLineFactory;
        this.orderRepository = orderRepository;
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
            orderLineFactory.create(lineDto, newOrder, store.getCode())
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
                subtotalAmount
            );
            discountCode = dto.getDiscountCode();
        }

        var deliveryFee = calculateDeliveryFee(dto.getOrderMethod());

        var totalAmount = subtotalAmount.add(deliveryFee).subtract(discountAmount);

        newOrder.setLines(lines);
        newOrder.setStoreCode(store.getCode());
        newOrder.setCustomerPhoneNum(dto.getCustomerPhoneNum());
        newOrder.setCustomerEmail(dto.getCustomerEmail());
        newOrder.setCustomerName(dto.getCustomerName());
        newOrder.setOrderMethod(dto.getOrderMethod());
        newOrder.setDestination(getDestination(dto.getOrderMethod(), dto.getDestination(), store));
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
            orderLineFactory.create(lineDto, newOrder, store.getCode())
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
                subtotalAmount
            );
            discountCode = dto.getDiscountCode();
        }

        var deliveryFee = calculateDeliveryFee(dto.getOrderMethod());

        var totalAmount = subtotalAmount.add(deliveryFee).subtract(discountAmount);

        if(dto.isPaid() && dto.getPaymentMethod() == null) {
            throw new DomainException(DomainExceptionCode.INVALID_PAYMENT_INFO);
        }

        newOrder.setId(UUID.randomUUID());
        newOrder.setLines(lines);
        newOrder.setStoreCode(store.getCode());
        newOrder.setCustomerPhoneNum(dto.getCustomerPhoneNum());
        newOrder.setCustomerEmail(dto.getCustomerEmail());
        newOrder.setCustomerName(dto.getCustomerName());
        newOrder.setOrderMethod(dto.getOrderMethod());
        newOrder.setDestination(getDestination(dto.getOrderMethod(), dto.getDestination(), store));
        newOrder.setStatus(dto.getStatus() == null ? OrderStatus.PREPARING : dto.getStatus());
        newOrder.setDiscountCode(discountCode);
        newOrder.setSubtotalAmount(subtotalAmount);
        newOrder.setDiscountAmount(discountAmount);
        newOrder.setDeliveryFee(deliveryFee);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setPaymentMethod(dto.getPaymentMethod());
        newOrder.setPaid(dto.isPaid());
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
    public OrderDto getOrderById(UUID id) {
        var order = orderRepository.findById(id).orElseThrow(
            () -> new DomainException(DomainExceptionCode.ORDER_NOT_FOUND)
        );
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderDto updateOrderStatus(UUID orderId, OrderStatus status) {
        var order = orderRepository.findById(orderId).orElseThrow(
            () -> new DomainException(DomainExceptionCode.ORDER_NOT_FOUND)
        );
        order.setStatus(status);
        if(status == OrderStatus.COMPLETED) {
            if(!order.isPaid()) {
                throw new DomainException(DomainExceptionCode.ORDER_NOT_PAID_YET);
            }
            order.setCompletedAt(Instant.now());
        }
        var savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public OrderDto updateOrderPayment(UUID orderId, PaymentMethod paymentMethod, Boolean paid) {
        var order = orderRepository.findById(orderId).orElseThrow(
            () -> new DomainException(DomainExceptionCode.ORDER_NOT_FOUND)
        );
        if(paymentMethod != null) {
            order.setPaymentMethod(paymentMethod);
        }
        if(paid != null) {
            if(order.getPaymentMethod() == null) {
                throw new DomainException(DomainExceptionCode.INVALID_PAYMENT_INFO);
            }
            order.setPaid(paid);
        }
        var savedOrder = orderRepository.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }

    /* TODO: temporary just for testing */
    private BigDecimal calculateDeliveryFee(OrderMethod method) {
        return method == OrderMethod.DELIVERY ? new BigDecimal("10000") : BigDecimal.ZERO;
    }

    private String getDestination(OrderMethod method, String destination, StoreDto store) {
        switch(method) {
            case PICK_UP -> {
                return store.getCode();
            }
            case DELIVERY -> {
                if(destination == null) throw new DomainException(DomainExceptionCode.ADDRESS_IS_INVALID);
                return destination;
            }
            default -> {
                throw new DomainException(DomainExceptionCode.ADDRESS_IS_INVALID);
            }
        }
    }
}
