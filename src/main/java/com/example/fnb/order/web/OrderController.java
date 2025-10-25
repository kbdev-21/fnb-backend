package com.example.fnb.order.web;

import com.example.fnb.order.OrderService;
import com.example.fnb.order.dto.OrderPreviewDto;
import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders/preview")
    public ResponseEntity<OrderPreviewDto> validateCard(@Valid @RequestBody CreateOrderDto createDto) {
        return ResponseEntity.ok(orderService.previewOrder(createDto));
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(createDto));
    }
}
