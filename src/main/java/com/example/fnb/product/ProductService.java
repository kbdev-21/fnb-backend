package com.example.fnb.product;

import com.example.fnb.product.dto.ProductCreateDto;
import com.example.fnb.product.dto.ProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(UUID id);
    ProductDto createProduct(ProductCreateDto productDto);
}
