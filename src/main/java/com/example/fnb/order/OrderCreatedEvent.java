package com.example.fnb.order;

import com.example.fnb.order.dto.OrderDto;
import org.springframework.context.ApplicationEvent;

public class OrderCreatedEvent extends ApplicationEvent {
    public OrderCreatedEvent(Object source, OrderDto order) {
        super(source);
    }
}
