package com.example.fnb.menu.domain;

import com.example.fnb.menu.domain.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ProductSpecification {
    public static Specification<Product> search(String key) {
        if(key == null || key.isBlank()) return null;
        return ((root, query, cb) -> {
            String pattern = "%" + key.toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("normalizedName")), pattern),
                cb.like(cb.lower(root.get("description")), pattern)
            );
        });
    }

    public static Specification<Product> hasCategoryId(UUID categoryId) {
        if (categoryId == null) return null;
        return (root, query, cb) -> cb.equal(root.get("categoryId"), categoryId);
    }
}
