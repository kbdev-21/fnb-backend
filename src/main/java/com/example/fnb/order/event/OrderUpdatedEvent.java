package com.example.fnb.order.event;

import com.example.fnb.order.dto.OrderDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
public class OrderUpdatedEvent extends ApplicationEvent {
    private UUID actorId;
    private OrderDto order;

    public OrderUpdatedEvent(Object source, UUID actorId, OrderDto order) {
        super(source);
        this.actorId = actorId;
        this.order = order;
    }
}
