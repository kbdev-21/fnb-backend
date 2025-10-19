package com.example.fnb.order;

import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import com.example.fnb.shared.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(CreateOrderDto createDto);
    List<OrderDto> getOrders();
    OrderDto updateOrderStatus(UUID orderId, OrderStatus status);
}
