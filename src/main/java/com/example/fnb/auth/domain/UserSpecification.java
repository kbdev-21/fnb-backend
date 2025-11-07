package com.example.fnb.auth.domain;

import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> search(String key) {
        if(key == null || key.isBlank()) return null;
        return ((root, query, cb) -> {
            String pattern = "%" + key.toLowerCase() + "%";

            return cb.or(
                cb.like(cb.lower(root.get("normalizedName")), pattern),
                cb.like(cb.lower(root.get("role")), pattern),
                cb.like(cb.lower(root.get("phoneNum")), pattern),
                cb.like(cb.lower(root.get("email")), pattern)
            );
        });
    }
}
