package com.example.fnb.order;

import org.springframework.context.ApplicationEvent;

public class OrderStatusChangeEvent extends ApplicationEvent {
    public OrderStatusChangeEvent(Object source) {
        super(source);
    }
}
