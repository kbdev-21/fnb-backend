package com.example.fnb.order.event;

import org.springframework.context.ApplicationEvent;

public class OrderStatusChangeEvent extends ApplicationEvent {
    public OrderStatusChangeEvent(Object source) {
        super(source);
    }
}
