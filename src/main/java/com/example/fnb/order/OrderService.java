package com.example.fnb.order;

import com.example.fnb.order.dto.OrderPreviewDto;
import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import com.example.fnb.shared.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderPreviewDto previewOrder(CreateOrderDto dto);
    OrderDto createOrder(CreateOrderDto dto);
    List<OrderDto> getOrders();
    OrderDto updateOrderStatus(UUID orderId, OrderStatus status);
}
