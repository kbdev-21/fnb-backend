package com.example.fnb.order.web;

import com.example.fnb.order.OrderService;
import com.example.fnb.order.dto.OrderPreviewDto;
import com.example.fnb.order.dto.CreateOrderDto;
import com.example.fnb.order.dto.OrderDto;
import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import com.example.fnb.shared.enums.PaymentMethod;
import com.example.fnb.shared.enums.UserRole;
import com.example.fnb.shared.security.SecurityUtil;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/api/orders/preview")
    public ResponseEntity<OrderPreviewDto> previewOrder(@Valid @RequestBody CreateOrderDto createDto) {
        return ResponseEntity.ok(orderService.previewOrder(createDto));
    }

    @PostMapping("/api/orders")
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto createDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(createDto));
    }

    @GetMapping("/api/orders")
    public ResponseEntity<Page<OrderDto>> getOrders(
        @RequestParam(required = false, defaultValue = "0") int pageNumber,
        @RequestParam(required = false, defaultValue = "20") int pageSize,
        @RequestParam(required = false, defaultValue = "-createdAt") String sortBy,
        @RequestParam(required = false, defaultValue = "") String searchKey,
        @RequestParam(required = false, defaultValue = "") String storeCode,
        @RequestParam(required = false, defaultValue = "") OrderMethod orderMethod,
        @RequestParam(required = false, defaultValue = "") OrderStatus status,
        @RequestParam(required = false, defaultValue = "") String discountCode,
        @RequestParam(required = false, defaultValue = "") String customerPhoneNum
    ) {
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN);

        return ResponseEntity.ok(orderService.getOrders(
            pageNumber,
            pageSize,
            sortBy,
            searchKey,
            storeCode,
            orderMethod,
            status,
            discountCode,
            customerPhoneNum
        ));
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable UUID id) {
        var order = orderService.getOrderById(id);
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        if (UserRole.STAFF.equals(SecurityUtil.getCurrentUserRole().orElse(null))) {
            SecurityUtil.onlyAllowStaffOfStoreCode(order.getStoreCode());
        }

        return ResponseEntity.ok(order);
    }

    @PatchMapping("/api/orders/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
        @PathVariable UUID id,
        @RequestParam(required = true) OrderStatus status
    ) {
        var order = orderService.getOrderById(id);
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        if (UserRole.STAFF.equals(SecurityUtil.getCurrentUserRole().orElse(null))) {
            SecurityUtil.onlyAllowStaffOfStoreCode(order.getStoreCode());
        }

        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }

    @PatchMapping("/api/orders/{id}/payment")
    public ResponseEntity<OrderDto> updateOrderPayment(
        @PathVariable UUID id,
        @RequestParam(required = false) PaymentMethod paymentMethod,
        @RequestParam(required = false) Boolean paid
    ) {
        var order = orderService.getOrderById(id);
        SecurityUtil.onlyAllowRoles(UserRole.ADMIN, UserRole.STAFF);
        if (UserRole.STAFF.equals(SecurityUtil.getCurrentUserRole().orElse(null))) {
            SecurityUtil.onlyAllowStaffOfStoreCode(order.getStoreCode());
        }

        return ResponseEntity.ok(orderService.updateOrderPayment(id, paymentMethod, paid));
    }
}
