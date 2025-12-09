package com.example.fnb.menu.event;

import com.example.fnb.menu.dto.ProductDto;
import com.example.fnb.order.dto.OrderDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
@Setter
public class ProductAvailabilityUpdatedEvent extends ApplicationEvent {
    private UUID actorId;
    private ProductDto product;
    private String storeCode;
    private boolean available;

    public ProductAvailabilityUpdatedEvent(Object source, UUID actorId, ProductDto product, String storeCode, boolean available) {
        super(source);
        this.actorId = actorId;
        this.product = product;
        this.storeCode = storeCode;
        this.available = available;
    }
}
