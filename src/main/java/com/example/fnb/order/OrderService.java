package com.example.fnb.order;

import com.example.fnb.order.domain.entity.Order;
import com.example.fnb.order.dto.CartDto;
import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import com.example.fnb.shared.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderDto createOrder(CreateOrderDto dto);
    List<OrderDto> getOrders();
    OrderDto updateOrderStatus(UUID orderId, OrderStatus status);
}
