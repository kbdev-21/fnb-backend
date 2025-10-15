package com.example.fnb.product;

import com.example.fnb.product.dto.ProductDto;

import java.util.UUID;

public interface ProductService {
    ProductDto getProductById(UUID id);
}
