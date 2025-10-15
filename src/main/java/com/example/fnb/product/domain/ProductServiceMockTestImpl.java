package com.example.fnb.product.domain;

import com.example.fnb.product.ProductService;
import com.example.fnb.product.dto.ProductDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Primary
public class ProductServiceMockTestImpl implements ProductService {
    @Override
    public ProductDto getProductById(UUID id) {
        System.out.println("Hello ");
        return new ProductDto(id, BigDecimal.TEN);
    }

}
