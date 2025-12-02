package com.example.fnb.discount.domain;

import com.example.fnb.order.event.OrderFulfilledAndPaidEvent;
import com.example.fnb.shared.exception.DomainException;
import com.example.fnb.shared.exception.DomainExceptionCode;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DiscountEventListener {
    private final DiscountRepository discountRepository;

    public DiscountEventListener(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @EventListener
    @Async
    public void handleOrderFulfilledAndPaidEvent(OrderFulfilledAndPaidEvent event) {
        var order = event.getOrder();
        if(order.getDiscountCode() == null) {
            return;
        }
        var discount =  discountRepository.findByCode(order.getDiscountCode()).orElseThrow(
            () -> new DomainException(DomainExceptionCode.DISCOUNT_NOT_EXISTED)
        );
        discount.setUsed(true);
        discount.setUsedByPhoneNum(order.getCustomerPhoneNum());
        discountRepository.save(discount);
    }
}
