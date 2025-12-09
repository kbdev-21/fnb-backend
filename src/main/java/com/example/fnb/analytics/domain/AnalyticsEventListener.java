package com.example.fnb.analytics.domain;

import com.example.fnb.menu.event.ProductAvailabilityUpdatedEvent;
import com.example.fnb.order.event.OrderUpdatedEvent;
import com.example.fnb.shared.enums.OrderStatus;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Component
public class AnalyticsEventListener {
    private final EventRepository eventRepository;

    public AnalyticsEventListener(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @EventListener
    @Async
    public void onProductAvailabilityUpdated(ProductAvailabilityUpdatedEvent event) {
        var metaData = new HashMap<String, String>();
        metaData.put("productId", event.getProduct().getId().toString());
        metaData.put("storeCode", event.getStoreCode());
        metaData.put("available", event.isAvailable() ? "true" : "false");

        var newEvent = new Event();
        newEvent.setId(UUID.randomUUID());
        newEvent.setActorId(event.getActorId());
        newEvent.setTitle("Product available status updated");
        newEvent.setDescription("User [" + event.getActorId() + "] updated availability of product [" + event.getProduct().getId() + "] at " + event.getStoreCode() + " to " + metaData.get("available") + ".");
        newEvent.setMetadata(metaData);
        newEvent.setOccurredAt(Instant.ofEpochMilli(event.getTimestamp()));

        eventRepository.save(newEvent);
    }

    @EventListener
    @Async
    public void handleOrderUpdatedEvent(OrderUpdatedEvent event) {
        var metaData = new HashMap<String, String>();
        metaData.put("orderId", event.getOrder().getId().toString());
        metaData.put("orderStatus", event.getOrder().getStatus().toString());
        metaData.put("orderPaid", event.getOrder().isPaid() ? "true" : "false");
        metaData.put("orderTotalAmount", event.getOrder().getTotalAmount().toString());

        var newEvent = new Event();
        newEvent.setId(UUID.randomUUID());
        newEvent.setActorId(event.getActorId());
        newEvent.setTitle("Order updated");
        newEvent.setDescription("User [" + event.getActorId() + "] updated order [" + event.getOrder().getId() + "].");
        newEvent.setMetadata(metaData);
        newEvent.setOccurredAt(Instant.ofEpochMilli(event.getTimestamp()));

        eventRepository.save(newEvent);
    }
}
