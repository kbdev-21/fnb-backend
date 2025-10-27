package com.example.fnb.product;

import com.example.fnb.product.dto.ToppingDto;

import java.util.UUID;

public interface ToppingService {
    ToppingDto getToppingById(UUID id);
}
