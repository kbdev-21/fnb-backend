package com.example.fnb.order.event;

import com.example.fnb.order.dto.OrderDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class OrderFulfilledAndPaidEvent extends ApplicationEvent {
    private OrderDto order;

    public OrderFulfilledAndPaidEvent(Object source, OrderDto order) {
        super(source);
        this.order = order;
    }
}
