package com.example.fnb.order.domain;

import com.example.fnb.order.domain.entity.Order;
import com.example.fnb.shared.enums.OrderMethod;
import com.example.fnb.shared.enums.OrderStatus;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {
    public static Specification<Order> search(String key) {
        if (key == null || key.isBlank()) return null;

        return (root, query, cb) -> {
            String pattern = "%" + key.toLowerCase() + "%";

            return cb.or(
                cb.like(
                    cb.function("text", String.class, root.get("id")),
                    pattern
                ),
                cb.like(cb.lower(root.get("customerPhoneNum")), pattern),
                cb.like(cb.lower(root.get("customerEmail")), pattern),
                cb.like(cb.lower(root.get("customerName")), pattern)
            );
        };
    }

    public static Specification<Order> hasStoreCode(String storeCode) {
        if (storeCode == null || storeCode.isBlank()) return null;

        return (root, query, cb) ->
            cb.equal(root.get("storeCode"), storeCode);
    }

    public static Specification<Order> hasOrderMethod(OrderMethod method) {
        if (method == null) return null;

        return (root, query, cb) ->
            cb.equal(root.get("orderMethod"), method);
    }

    public static Specification<Order> hasStatus(OrderStatus status) {
        if (status == null) return null;

        return (root, query, cb) ->
            cb.equal(root.get("status"), status);
    }

    public static Specification<Order> hasDiscountCode(String discountCode) {
        if (discountCode == null || discountCode.isBlank()) return null;

        return (root, query, cb) ->
            cb.equal(root.get("discountCode"), discountCode);
    }

    public static Specification<Order> hasCustomerPhoneNum(String phoneNum) {
        if (phoneNum == null || phoneNum.isBlank()) return null;

        return (root, query, cb) ->
            cb.equal(root.get("customerPhoneNum"), phoneNum);
    }
}
