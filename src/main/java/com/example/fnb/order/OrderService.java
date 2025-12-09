package com.example.fnb.order;

import com.example.fnb.order.dto.OrderPreviewDto;
import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import com.example.fnb.shared.enums.PaymentMethod;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderPreviewDto previewOrder(CreateOrderDto dto);
    OrderDto createOrder(CreateOrderDto dto);
    Page<OrderDto> getOrders(
        int pageNumber,
        int pageSize,
        String sortBy,
        String searchKey,
        String storeCode,
        OrderMethod orderMethod,
        OrderStatus status,
        String discountCode,
        String customerPhoneNum
    );
    OrderDto getOrderById(UUID id);
//    OrderDto updateOrderStatus(UUID orderId, OrderStatus status);
//    OrderDto updateOrderPayment(UUID orderId, PaymentMethod paymentMethod, Boolean paid);
    OrderDto updateOrder(UUID orderId, OrderStatus status, PaymentMethod paymentMethod, Boolean paid);
}
