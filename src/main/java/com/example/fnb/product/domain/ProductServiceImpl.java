package com.example.fnb.product.domain;

import com.example.fnb.product.ProductService;
import com.example.fnb.product.dto.ProductDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public ProductDto getProductById(UUID id) {
        return null;
    }
}